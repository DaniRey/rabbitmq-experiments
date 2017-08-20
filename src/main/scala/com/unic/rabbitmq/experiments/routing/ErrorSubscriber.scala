package com.unic.rabbitmq.experiments.routing

import java.io.IOException

import com.rabbitmq.client.{AMQP, ConnectionFactory, DefaultConsumer, Envelope}
import com.unic.rabbitmq.experiments.routing.Severity.Err

object ErrorSubscriber extends App {
  val factory = new ConnectionFactory
  factory.setHost("localhost")
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  val exchangeName = "logsWithSeverity"
  val queueName = "" //none or defaultl
  val exchange = channel.exchangeDeclare(exchangeName, "direct")
  val queue = channel.queueDeclare(queueName, false, true, true, null)
  channel.queueBind(queueName, exchangeName, Err.toString)

  val printingConsumer = new DefaultConsumer(channel) {

    @throws[IOException]
    override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]): Unit = {
      val message = new String(body, "UTF-8")
      println(s"ERROR received '$message' with routing key ${envelope.getRoutingKey}")
    }
  }

  channel.basicConsume(queueName, true, printingConsumer)
}
