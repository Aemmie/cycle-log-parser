# The Cycle: Frontier log parser


UI with usefull info such as:
* Unique instance name (derived from instance UUID)
* Players count (nearby / total)
* Time to: morning / sunrise / rain / storm / server death
* Kill feed
* Evac countdown

Works by reading game log file (\AppData\Local\Prospect\Saved\Logs\Prospect.log).

Tested on TCF 3.2.0, Win10, OpenJDK 17, 2560x1440.

# How to use
1. Take latest JAR from releases (or build it by yourself with `mvn clean package shade:shade`)
2. Make sure you have Java **17+**
3. Open CMD and `cd` to directory of your jar
4. Launch it by typing `java -jar cycle.jar`

# What does it look like
(top right corner)

![изображение](https://user-images.githubusercontent.com/10757826/233166138-852cddba-94b5-447a-a322-b970a787f481.png)
