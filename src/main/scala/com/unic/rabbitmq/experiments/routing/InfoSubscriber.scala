package com.unic.rabbitmq.experiments.routing

import java.io.IOException

import com.rabbitmq.client.{AMQP, ConnectionFactory, DefaultConsumer, Envelope}

object InfoSubscriber extends App {
  val factory = new ConnectionFactory
  factory.setHost("localhost")
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  val exchangeName = "logsWithSeverity"
  val queueName = "" //none or defaultl
  val exchange = channel.exchangeDeclare(exchangeName, "direct")
  val queue = channel.queueDeclare(queueName, false, true, true, null)

  Severity.values.foreach(severity => channel.queueBind(queueName, exchangeName, severity.toString))

  val printingConsumer = new DefaultConsumer(channel) {

    @throws[IOException]
    override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]): Unit = {
      val message = new String(body, "UTF-8")
      println(s"Received '$message' with routing key ${envelope.getRoutingKey}")
    }
  }

  channel.basicConsume(queueName, true, printingConsumer)
}
