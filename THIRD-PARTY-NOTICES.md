# Third-Party Notices

This repository's original application source code is licensed under the Apache License, Version 2.0. The following components are provided under their own licenses and are not relicensed by this repository.

## H2 Database Engine 1.4.200

The application packages `com.h2database:h2:1.4.200` in its WAR for the embedded database used by the sample.

- Project: https://www.h2database.com/
- License: Eclipse Public License 1.0 or Mozilla Public License 2.0 (dual license)
- License information: https://www.h2database.com/html/license.html

## Jakarta Servlet API 6.0.0 and Jakarta Server Pages API 3.1.1

These APIs are Maven dependencies with `provided` scope. They are supplied by the Liberty runtime and are not packaged in the application's WAR.

- Project: https://jakarta.ee/
- License information: https://jakarta.ee/legal/

## Open Liberty / IBM WebSphere Application Server Liberty runtime

The supplied `Containerfile` uses the Open Liberty container image. The application source code in this repository does not include the Liberty runtime. Users of the container image must comply with the license terms applicable to the selected runtime image.

- Open Liberty: https://openliberty.io/
- IBM WebSphere Application Server Liberty: https://www.ibm.com/products/websphere-application-server/liberty

## Test-only dependencies

JUnit 4.13.2 and Mockito 4.11.0 are used only for tests and are not included in the application WAR.
