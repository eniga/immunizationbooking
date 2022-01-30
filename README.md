# MQTT Traffic light
[![NuGet](https://img.shields.io/badge/nuget-v1.0.0-blue)](https://www.nuget.org/packages/NubanLibrary)
[![License MIT](https://img.shields.io/badge/license-MIT-green.svg)](https://opensource.org/licenses/MIT)

Making Traffic light data available to IoT devices using Raspberry Pi Ki and Python

Note that this program is expected to run on a Raspberry Kit

## Content
```
- main.py (This contains the source code to run the entire program. It could also be seen as the entrypoint)
- trafficlight.py (This is the source code to control the lights on the Raspberry Pi KIt. It contains functions for each light)
- publish.py (This is the MQTT program which contains a function to publish data using a public broker. The broker and topic can be changed in the main.py file)
- subscribe.py (This is hte MQTT program whcih contains the function to subscribe to a topic using a public broker. The broker and topic can be changed in the main.py file)
- 20026427.docx (This contains the summary of this course work)
- flows.json (Node-Red export file to test the MQTT process)
```

## Contributors

* [Eniga Ahiante](https://github.com/eniga) - 20026427
