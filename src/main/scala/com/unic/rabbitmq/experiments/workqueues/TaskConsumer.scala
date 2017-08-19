package com.unic.rabbitmq.experiments.workqueues

import java.io.IOException

import com.rabbitmq.client.{AMQP, ConnectionFactory, DefaultConsumer, Envelope}

object TaskConsumer extends App {
  val factory = new ConnectionFactory
  factory.setHost("localhost")
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  val queueName = "tasks"
  val prefetchCount = 1
  private val durable = true
  channel.queueDeclare(queueName, durable, false, false, null)
  //make sure every taskConsumer only fetches one task at a time from the channel
  channel.basicQos(prefetchCount)

  val printingConsumer = new DefaultConsumer(channel) {

    @throws[IOException]
    override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]): Unit = {
      val message = new String(body, "UTF-8")
      println(s"Received '$message'")

      try {
        doWork(message)
      } finally {
        println(s"Finished task '$message'")
        channel.basicAck(envelope.getDeliveryTag, false)
      }
    }
  }

  private val autoAck = false
  channel.basicConsume(queueName, autoAck, printingConsumer)


  @throws[InterruptedException]
  private def doWork(task: String): Unit = {
    for (ch <- task.toCharArray) {
      if (ch == '.') Thread.sleep(1000)
    }
  }
}
