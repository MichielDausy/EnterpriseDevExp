# Speedrun Tracker
De Speedrun Tracker applicatie is een handige tool ontworpen voor speedrunners om hun voortgang en prestaties bij te houden tijdens het spelen van games. Of je nu een beginner bent of een ervaren speedrunner, deze app is ontwikkeld om het proces van het monitoren en analyseren van je speedruns te vergemakkelijken.

Met deze applicatie kan je dus een speedrun voor een game starten en beëindigen waarbij de tijd wordt berekent en deze te zien kan zijn met alle andere speedruns voor die game.
## Structuur
De applicatiestructuur ziet er als volgt uit:

![Speedrun](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/ddce5ac4-3035-443f-9b37-eceb346a4e87)

Je kan zien dat er 3 microservices en 1 api-gateway is.
Elke service heeft zowat dezelfde bestanden zoals controllers, dto's, models, repositories en services.

De controller is eigenlijk het toegangspunt tot de service, alle request die naar een service gaan komen eerst in de controller terecht. In zo'n controller staan alle endpoints van de service, als deze endpoints gebruikt worden zal de controller de service files gebruiken om de achterliggende logica te draaien.


In een service file wordt de logica dat moet gebeuren uitgevoerd alsook de communicatie tussen de microservices, bijvoorbeeld voor het deleeten van een game moet er eerst gecheckt worden of dat deze game niet meer in gebruik is voor een speedrun. Aangezien dat deze informatie niet in de game-service maar in de speedrun-service staat moet er een request gestuurd worden naar die service:

![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/8c450b16-0640-4ff1-b4db-45bd071d94fa)

Om zowel data te ontvangen en te versturen worden dto's gebruikt.

Om data uit een database te halen worden repositories gebruikt. De service files gebruiken deze repositories om de nodige data te krijgen.

De 3 microservices die gebruikt worden in deze applicatie zijn:
- Game-service - MongoDB database
- Speedrun-service - MySQL database
- Profile-service - MySQL database
### Game-service
De game-service is op poort 8080 toegankelijk voor de andere services en de API-gateway. in deze service heb je deze endpoints:
- ***/all***: geeft alle games van de applicatie terug 
- ***/getid***: wordt enkel door de speedrun-service aangeroepen als er een nieuwe speedrun aangemaakt word. Het stuurt voor de gekozen game de ID (niet primary key maar een aparte waarde) mee.
- ***/top5***: Dit is de belangrijkste endpoint voor deze service. voor een ingegeven game wordt er een request gestuurd naar de speedrun-service om de 5 beste speedruns van deze game te verzamelen. In de speedrun-service staat de logica om de top 5 speedruns te verkrijgen waarbij een request wordt gestuurd van speedrun-service naar de profile-service om ook de profiles erbij te kunnen zien
- ***/create***: Maakt een nieuwe game aan
- ***/update***: Wijzigt een bestaande game
- ***/delete***: Verwijderd een bestaande game. Het wijzigen lukt dus enkel als er geen speedruns meer aan gekoppeld zijn
### Speedrun-service
De speedrun-service is op poort 8081 toegankelijk voor de andere services en de API-gateway. in deze service heb je deze endpoints:
- ***/start***: Hier wordt er een gloednieuwe speedrun aangemaakt en wordt er een tijd bijgehouden die we de startijd noemen. Bij het aanmaken van een speedrun wordt er een game naam en profiel meegegeven, deze naam herkent de speedrun-service niet omdat ze niet in de database van deze service zitten, enkel de ID's ervan. Daarom wordt er een request naar de game-service en profile-service gestuurd om deze ID's te krijgen.
- ***/end***: Door de ID van de speedrun (dat getoond wordt tijdens het aanmaken van een speedrun) mee te geven zal deze speedrun eindigen. Een eindtijd zal bijgehouden worden en de tijdsduur tussen deze 2 is de uiteindelijke tijd waarin de game gespeeld is.
## Technologieën gebruikt
- Spring boot
- MySQL
- MongoDB
- Junit
- Kubernetes
- Prometheus
- Grafana
