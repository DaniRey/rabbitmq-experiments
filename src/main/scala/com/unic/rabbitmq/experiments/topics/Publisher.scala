package com.unic.rabbitmq.experiments.topics

import com.rabbitmq.client.ConnectionFactory
import com.unic.rabbitmq.experiments.topics.Severity._
import com.unic.rabbitmq.experiments.topics.Facility._

import scala.util.Random

object Publisher extends App {
  val factory = new ConnectionFactory
  factory.setHost("localhost")
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  val exchangeName = "logsTopic"
  val queueName = "" //none/default
  channel.exchangeDeclare(exchangeName, "topic")

  val messages = LogMessage(Err, Kern,  "kernel panic") :: LogMessage(Warn, Auth, "user X locked for too many invalid login attempts") ::
    LogMessage(Info, Auth, "Invalid login attempt by user Y") :: LogMessage(Info, Kron, "job X start") :: LogMessage(Info, Kron, "job X finished") :: Nil


  (1 to 10).foreach{_ =>
    val message = Random.shuffle(messages).head
    channel.basicPublish(exchangeName, message.topic, null, message.message.getBytes())
    println(s"sending '$message'")
  }


  channel.close()
  connection.close()
}
