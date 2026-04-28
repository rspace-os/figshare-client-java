# Agent Instructions — figshare-client-java

## Integration Tests

- **Always run the acceptance/integration tests** (`FigshareAcceptanceTest`) after making changes — do not rely on unit tests alone.
- The tests require a **Figshare personal API token**. Ask the user for it before running.
- Pass the token via Maven system property:
  ```
  mvn test -Dtest="FigshareAcceptanceTest" -DfigshareToken=<TOKEN>
  ```
- `testPrivateLinkArticleCRUD` may fail with `403 Insufficient permissions` if the Figshare account is not linked to an ORCID account — this is a known pre-existing issue, not a code bug.

## Security

- The Figshare API token is a **secret**. Never commit it to the repository, embed it in source files, or push it to Git in any form.
