#!/bin/sh
# launcher.sh
# export GPIOs

echo 199 > /sys/class/gpio/export
echo in > /sys/class/gpio/gpio199/direction
echo 200 > /sys/class/gpio/export
echo in > /sys/class/gpio/gpio200/direction
echo 204 > /sys/class/gpio/export
echo in > /sys/class/gpio/gpio204/direction
java -jar /root/PROJECT_MAIN/GPIO.jar
