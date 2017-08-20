package com.unic.rabbitmq.experiments.topics

import com.unic.rabbitmq.experiments.topics.Facility.Facility
import com.unic.rabbitmq.experiments.topics.Severity.Severity

case class LogMessage(severity: Severity, facility: Facility, message: String) {
  def topic: String = severity.toString + "." + facility.toString
}
