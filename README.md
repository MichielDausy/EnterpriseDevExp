# Speedrun Tracker
De Speedrun Tracker applicatie is een handige tool ontworpen voor speedrunners om hun voortgang en prestaties bij te houden tijdens het spelen van games. Of je nu een beginner bent of een ervaren speedrunner, deze app is ontwikkeld om het proces van het monitoren en analyseren van je speedruns te vergemakkelijken.

Met deze applicatie kan je dus een speedrun voor een game starten en beëindigen waarbij de tijd wordt berekent en deze te zien kan zijn met alle andere speedruns voor die game.
## Structuur
De applicatiestructuur ziet er als volgt uit:

![Speedrun](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/ddce5ac4-3035-443f-9b37-eceb346a4e87)

Je kan zien dat er 3 microservices en 1 api-gateway is. De 3 microservices zijn onderverdeelt in:
- Game-service - MongoDB
- Speedrun-service - MySQL
- Profile-service - MySQL


Elke service heeft zowat dezelfde bestanden zoals controllers, dto's, models, repositories en services.

De controller is eigenlijk het toegangspunt tot de service, alle request die naar een service gaan komen eerst in de controller terecht. In zo'n controller staan alle endpoints van de service, als deze endpoints gebruikt worden zal de controller de service files gebruiken om de achterliggende logica te draaien.


In een service file wordt de logica dat moet gebeuren uitgevoerd alsook de communicatie tussen de microservices, bijvoorbeeld voor het deleeten van een game moet er eerst gecheckt worden of dat deze game niet meer in gebruik is voor een speedrun. Aangezien dat deze informatie niet in de game-service maar in de speedrun-service staat moet er een request gestuurd worden naar die service:

![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/8c450b16-0640-4ff1-b4db-45bd071d94fa)

Om zowel data te ontvangen en te versturen worden dto's gebruikt.

Om data uit een database te halen worden repositories gebruikt. De service files gebruiken deze repositories om de nodige data te krijgen.
### Game-service
In de game-service 
## Features
De 3 belangrijkste features van de applicatie zijn de start/stop, leaderboard en API-gateway functionaliteiten
- Start en stop functionaliteit: Voor deze functionaliteit zijn er 2 endpoints gemaakt:
  - /start: Hier wordt er een gloednieuwe speedrun aangemaakt en wordt er een tijd bijgehouden die we de startijd noemen.
  - /end: een request naar deze endpoint zorgt ervoor dat de eindtijd bepaald word. Het tijdsverschil tussen de startijd en eindtijd is de effectieve tijdsduur van de speedrun.
- Top 5 leaderboard van games:
- API-gateway: 
## Technologieën gebruikt
- Spring boot
- MySQL
- MongoDB
- Junit
- Kubernetes
- Prometheus
- Grafana
