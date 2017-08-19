package com.unic.rabbitmq.experiments.trivial

import java.io.IOException

import com.rabbitmq.client.{AMQP, ConnectionFactory, DefaultConsumer, Envelope}

object Consumer extends App {
  val factory = new ConnectionFactory
  factory.setHost("localhost")
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  val queueName = "hello"
  channel.queueDeclare(queueName, false, false, false, null)

  val printingConsumer = new DefaultConsumer(channel) {

    @throws[IOException]
    override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]): Unit = {
      val message = new String(body, "UTF-8")
      println(s"Received '$message'")
    }
  }

  channel.basicConsume(queueName, true, printingConsumer)
}
