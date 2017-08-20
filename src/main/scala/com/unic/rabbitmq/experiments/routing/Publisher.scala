package com.unic.rabbitmq.experiments.routing

import com.rabbitmq.client.ConnectionFactory
import com.unic.rabbitmq.experiments.routing.Severity._

import scala.util.Random

object Publisher extends App {
  val factory = new ConnectionFactory
  factory.setHost("localhost")
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  val exchangeName = "logsWithSeverity"
  val queueName = "" //none/default
  channel.exchangeDeclare(exchangeName, "direct")

  val messages = (Err, "bad stuff happened") :: (Warn, "might be a problem") :: (Warn, "strange stuff going on") :: (Info, "started X") :: (Info, "stopped X") :: Nil
  val message = Random.shuffle(messages).head

  channel.basicPublish(exchangeName, message._1.toString, null, message._2.getBytes())

  println(s"sending '$message'")

  channel.close()
  connection.close()
}
