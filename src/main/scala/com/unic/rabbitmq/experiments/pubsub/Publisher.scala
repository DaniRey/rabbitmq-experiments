package com.unic.rabbitmq.experiments.pubsub

import com.rabbitmq.client.ConnectionFactory

import scala.util.Random

object Publisher extends App {
  val factory = new ConnectionFactory
  factory.setHost("localhost")
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  val exchangeName = "logs"
  val queueName = "" //none/default
  channel.exchangeDeclare(exchangeName, "fanout")

  val message = Random.alphanumeric.take(10).foldLeft("")(_ + _)
  channel.basicPublish(exchangeName, queueName, null, message.getBytes())

  println(s"sending '$message'")

  channel.close()
  connection.close()
}
