package com.unic.rabbitmq.experiments.pubsub

import java.io.IOException

import com.rabbitmq.client.{AMQP, ConnectionFactory, DefaultConsumer, Envelope}

import scala.util.Random

object Subscriber extends App {
  val factory = new ConnectionFactory
  factory.setHost("localhost")
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  val exchangeName = "logs"
  val queueName = "" //none or defaultl
  val exchange = channel.exchangeDeclare(exchangeName, "fanout")
  val queue = channel.queueDeclare(queueName, false, true, true, null)
  channel.queueBind(queueName, exchangeName, "")

  val printingConsumer = new DefaultConsumer(channel) {

    @throws[IOException]
    override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]): Unit = {
      val message = new String(body, "UTF-8")
      println(s"Received '$message'")
    }
  }

  channel.basicConsume(queueName, true, printingConsumer)
}
