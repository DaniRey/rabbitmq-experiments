package com.unic.rabbitmq.experiments.workqueues

import com.rabbitmq.client.ConnectionFactory

import scala.util.Random

object TaskProducer extends App {
  val factory = new ConnectionFactory
  factory.setHost("localhost")
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  val queueName = "tasks"
  val tasks = "Rather complex ....." :: "Simple ." :: "Intermediate ..." :: Nil
  val message = Random.shuffle(tasks).head

  private val durable = true
  channel.queueDeclare(queueName, durable, false, false, null)
  channel.basicPublish("", queueName, null, message.getBytes())

  println(s"sending '$message'")

  channel.close()
  connection.close()
}
