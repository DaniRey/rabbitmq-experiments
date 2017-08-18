package com.unic.rabbitmq.experiments.trivial

import com.rabbitmq.client.ConnectionFactory

object Producer extends App {
  val factory = new ConnectionFactory
  factory.setHost("localhost")
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  val queueName = "hello"
  val message = "Hello World"
  channel.queueDeclare(queueName, false, false, false, null)
  channel.basicPublish("", queueName, null, message.getBytes())

  println(s"sending '$message'")

  channel.close()
  connection.close()
}
