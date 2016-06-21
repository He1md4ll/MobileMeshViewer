# MobileMeshViewer

MobileMeshViewer ermöglicht die Freifunk Knotenstatistik auf dem Mobiltelefon abzurufen und zu überwachen. 
Die App war ein Projekt für "Mobile Computing" an der Hochschule Bremen. Sie wurde entwickelt von Martin und Jelto Wodstrcil.
Der MobileMeshViewer bietet:

* Anzeige der detailierten Knotenstatisiken
* Anzeige der detailierten Gatewaystatistiken
* Notifications falls ein bestimmter Knoten oder ein Gateway offline geht
* Anpassbare Notifications
* Links zu Social Media und Mail/IRC

<p align="center">
  <img src="docs/screenshot_drawer.png?raw=true" width="200"/>
  <img src="docs/screenshot_nodes.png?raw=true" width="200"/>
  <img src="docs/screenshot_node_detail.png?raw=true" width="200"/>
  <img src="docs/screenshot_about.png?raw=true" width="200"/>
</p>

## Installation

Zurzeit ist die App leider nur als [.apk im Repository](./app-debug.apk?raw=true) verfügbar. 
Die App wurde mit einer SDK-Version von 23 compiliert und setzt mindestens SDK-Version 14 (Android 4.0) vorraus.
Eine Veröffentlichung im Play Store oder auf den offiziellen Freifunk Bremen Servern ist denkbar.

## API

Die von der App angezeigten Statistiken werden über externe JSON-Dateien geladen. 
Daten der Freifunk-Knoten werden über die [nodelist.json](https://downloads.bremen.freifunk.net/data/nodelist.json) und die [nodes.json](https://downloads.bremen.freifunk.net/data/nodes.json) geladen. 
Die Daten über die Freifunk-Gateways stammen zu einem Teil aus der [gatemon-json](https://status.bremen.freifunk.net/data/merged.json) und zum anderen ebenfalls aus der [nodes.json](http://downloads.bremen.freifunk.net/data/nodes.json).

## Knotenliste
Die Knotenliste ist die Einstiegsseite der App. In einem [Fragment](https://github.com/He1md4ll/MobileMeshViewer/blob/master/app/src/main/java/freifunk/bremen/de/mobilemeshviewer/node/NodeListFragment.java) werden alle Freifunk-Knoten angezeigt. Über swipes lässt sich die Liste manuell aktualisieren. Über die Lupe kann die Liste nach bestimmten Knoten-Namen durchsucht werden. Sobald ein Knoten in der Listenansicht ausgewählt wird, öffnet sich die Detailansicht.

<p align="center">
  <img src="docs/screenshot_nodes.png?raw=true" width="200"/>
</p>

## Knotendetails
Die Detailansicht präsentiert (fast) alle Statistiken, die in der [nodes.json](http://downloads.bremen.freifunk.net/data/nodes.json) enthalten sind. Das sind z.B. die MAC- und IP-Adressen oder der hinterlegte Kontakt. Wenn der Knoten online ist, erscheint der Name in der Toolbar grün. Falls er nicht am Netz ist, fehlen einige Informationen und der Name ist rot eingefärbt.

Über den "+"-Button lässt sich ein Knoten markieren. Alle markierten Knoten werden auf Konnektivität überwacht. Die Liste der markierten Knoten kann über das Menü erreicht werden.

<p align="center">
  <img src="docs/screenshot_node_detail.png?raw=true" width="200"/>
</p>

## Knoten Überwachen
Über die Einstellungen (Aktivitiy [hier](https://github.com/He1md4ll/MobileMeshViewer/blob/master/app/src/main/java/freifunk/bremen/de/mobilemeshviewer/SettingsActivity.java)) lässt sich die Überwachung anpassen. Es kann der Aktualisierungs-Intervall gewählt werden, die Art der Notification und ob Gateways auch mit überwacht werden sollen.

<p align="center">
  <img src="docs/screenshot_settings.png?raw=true" width="200"/>
</p>

## Gateway-Liste

<p align="center">
  <img src="docs/screenshot_gateways.png?raw=true" width="200"/>
</p>

## Gateway-Details

<p align="center">
  <img src="docs/screenshot_gateway_detail.png?raw=true" width="200"/>
</p>

## Frameworks und technisches

## Zukunft
