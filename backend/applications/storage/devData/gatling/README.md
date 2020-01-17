# Gatling

## Features

The Gatling application is used to ease load and performance tests made with Gatling:

* Easily **import HAR files** or Gatling .scala test scripts,
* **Compare test executions** with records (or compare successive test executions),
* Visualize results during the load test execution thanks to **Grafana**.

Learn more about Kraken Gatling features and usage in <a href="/doc/gatling/" target="_blank">the documentation</a>.

## Getting Started

### How to Run a Load Test

1. Open the Simulations tree, on the left,
2. Select a `.scala` file,
3. Click on the _Play_ icon next to the filename,
4. Fill in the _Run Simulation_ dialog and click on _Run_.

Once the test is running, you can <a href="/doc/gatling/analyze-gatling-load-test/" target="_blank">open the Grafana dashboard</a>.
The default credentials are `admin/kraken`.

**Need Help? Click on the Help button** at the right of the window to open the contextual Help.

### How to Debug a Gatling Script

Much like running a load test, but this time click on the _Bug_ icon next to the simulation name.

Once the debug has started, you will be able to <a href="/doc/editors/debug-request-response/" target="_blank">check sent request and corresponding server responses</a>.