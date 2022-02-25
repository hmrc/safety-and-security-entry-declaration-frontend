
# safety-and-security-entry-declaration-frontend

This is a placeholder README.md for a new repository

## Creating new pages

A number of giter8 scaffolds are provided to help create new pages easily. These generate all the new classes you should
need to have a working page, form, etc., depending on the type of page you're creating, and also creates a migration script
which you can run to do the extra work of modifying files needed to add new pages, e.g. adding your new routes, setting
some placeholder messages, adding page definitions to test generator utils, etc.

You can add a new page by running `sbt 'g8Scaffold stringPage'`, for example, filling out the values you're prompted for,
and then running `./migrate.sh` to run the generated migration script as well.

You can see the different types of supported pages in the `.g8` directory.

## Scalafmt

We have a scalafmt config in the project to help maintain code readability. There are some limitations to
what scalafmt can fix but the config provided generally does a good job of keeping things readable.

To apply the scalafmt config, simply run `sbt scalafmt`.

## Running tests in Intellij
In order to run the tests in Intellij, you should add the following to your test run configurations under "VM Options":
```
-Dconfig.resource=test.application.conf
```

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
