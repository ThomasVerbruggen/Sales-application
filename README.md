# Mosselen verkoop

## Bedoeling

Het programma laat toe om een aantal artikels te definiëren, met een omschrijving en de prijs. 

In een tweede tabblad wordt de verkoop beheerd. Dit scherm toont de lijst van verkoopbare artikels en met behulp
van de knoppen kan het aantal van het geselecteerde artikel gewijzigd worden. Het is ook mogelijk om voor dat artikel
een aantal gratis porties op te geven (voor de helpers). Bij het afronden van een verkoop wordt er automatisch een 
ticket afgedrukt.
Het is steeds mogelijk om het ticketje van een verkoop opnieuw te printen.

In eer derde tabblad is het mogelijk om de verkoopsresultaten van een bepaalde periode te berekenen. Dit kan ook als
PDF bestand uitgeprint worden.

## Architectuur

Het programma is een Spring Boot toepassing met een extra javaFx user interface. Het gebruikt een SQL database om de
artikels en de verkooplijnen op te slaan.

Het programma is net omgezet naar Spring Boot 2.7.0, nadat het 5 jaar ongewijzigd bleef. Het **vereist** nog wel dat
het gebuild en gerund worden op een **JDK 1.8**. 

Het programma wordt geconfigureerd door properties, te vinden in de `application.yml` file. Bij het opstarten kunnen andere
waarden voor de properties opgegeven worden of de properties file kan aangepast worden.

``` yaml
logging:
  level:
    org.springframework: ERROR
    org.hibernate: ERROR
    janvdl: INFO

format:
  amount: "###,##0.00 \u20ac"
  quantity: "###,##0"
  date: d/MM/yyyy HH:mm

printer:
  ### real printer
  # name: com1
  ### windows - file
  name: c:\\temp\\printer.txt


javafx:
  title: Mosselen

spring:
  datasource:
    url: jdbc:h2:C:\\Temp\\sales-2022\\data
    username: java
    password: java_123
    driver-class-name: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate.ddl-auto: update

  h2:
    console:
      enabled: true
      path: /h2-console
      settings:
        trace: false
        web-allow-others: true

management:
  endpoints:
    web:
      exposure:
        include: "*"
  
  endpoint:
    health:
      show-details: always
      show-components: always
```

Als het programma draait zijn er web-endpoints beschikbaar op poort 8080.

- Met het endpoint `/actuator` kan alle informatie over het programma opgehaald worden.
- Met het endpoint `/h2-console` kan de database bekeken worden. (Dit kan natuurlijk ook zonder dat het programma draait
met een algemene DBManager, b.v. DBeaver).

- Bekijken/Editen van de database gaat via `http://localhost:8080/h2-console` met username `java` en password `java_123`.


Voorbeeld

```
curl http://localhost:8080/actuator/health
```
``` json
{
	"status": "UP",
	"components": {
		"db": {
			"status": "UP",
			"details": {
				"database": "H2",
				"validationQuery": "isValid()"
			}
		},
		"diskSpace": {
			"status": "UP",
			"details": {
				"total": 499677917184,
				"free": 310455173120,
				"threshold": 10485760,
				"exists": true
			}
		},
		"ping": {
			"status": "UP"
		}
	}
}
```

Een `pom.xml` is gedefinieerd om het met maven te builden: `mvn clean package`. Hiermee wordt in de `target` folder van 
het project een jar file gemaakt `sales-appl-1.2.0.jar`.

Deze jar file kan opgestart worden om het programma op te starten: `java -jar sales-appl-1.2.0.jar`

Om dit programma eenvoudig standalone op te kunnen starten, heb ik een H2 database driver toegevoegd en geconfigureerd. 
Dit is een embedded database die zijn gegevens rechtstreeks naar schijf schrijft (in dit geval naar `C:\Temp\sales-2022`),
dus het is niet nodig om een DB Service te draaien.

## Printer

Zolang er geen ticket printer geconnecteerd is, staat de property `printer.name` gedefinieerd als een bestandsnaam. De
waarde in de properties file is nu `c:\temp\printer.txt`. Ieder ticketje wordt nu naar dit bestand geschreven.
Later te vervangen door `com1`.

```
    8         29-mei-2022          21:31


  4 MOSFR Mosselen met Frit        80,00

  1 MOSFR Mosselen met Frit         0,00

  1 MOSBR Mosselen met brood       20,00

  1 CROQ  Croque Monsieur          17,00

  1 CURR  Curryworst               15,00

 10 IJS   Ijsje                    50,00

  3 DRANK Drankkaart               45,00

          Totaal                  227,00


    Deze bon afgeven aan de helpers aub

```

