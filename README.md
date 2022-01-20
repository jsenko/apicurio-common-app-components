[![Verify Build Workflow](https://github.com/Apicurio/apicurio-common-app-components/workflows/Verify%20Build%20Workflow/badge.svg)](https://github.com/Apicurio/apicurio-common-app-components/actions?query=workflow%3A%22Verify+Build+Workflow%22)
[![Automated Release Notes by gren](https://img.shields.io/badge/%F0%9F%A4%96-release%20notes-00B2EE.svg)](https://github-tools.github.io/github-release-notes/)

# Apicurio Common App Components

## What is it?
This project contains a set of components that are useful and common across multiple Apicurio applications.
Some of the modules contained in this repository include:

* **Auth** : custom authentication and authorization handlers
* **Configuration** : support for dynamic configuration properties
* **Logging** : some core logging functionality
* **Maven Plugin** : maven plugin(s) used by other Apicurio project builds
* **Multi-tenancy** : support for multi-tenancy in multi-tenant Apicurio applications
* **SQL Storage** : common SQL storage functionality
* **Web Utilities** : a set of useful web utilites (e.g. cache control, HSTS, etc)

This project is licensed under the [Apache License 2.0](LICENSE).

## Contribute Fixes and Features
This project is open source, and we welcome anybody who wants to participate and contribute!

### Get the code
The easiest way to get started with the code is to [create your own fork](http://help.github.com/forking/)
of this repository, and then clone your fork:

```bash
$ git clone git@github.com:<you>/apicurio-common-app-components.git
$ cd apicurio-common-app-components
$ git remote add upstream git://github.com/Apicurio/apicurio-common-app-components.git
```

At any time, you can pull changes from the upstream and merge them onto your master:

```bash
$ git checkout master       # switches to the 'master' branch
$ git pull upstream master  # fetches all 'upstream' changes and merges 'upstream/master' onto your 'master' branch
$ git push origin           # pushes all the updates to your fork, which should be in-sync with 'upstream'
```

The general idea is to keep your 'master' branch in-sync with the 'upstream/master'.

### Track Your Change
If you want to fix a bug or make any changes, please log an issue in the github 
[Issue Tracker](https://github.com/Apicurio/apicurio-common-app-components/issues) describing the bug or new 
feature. Then we highly recommend making the changes on a topic branch named with the issue 
number. For example, this command creates a branch for issue #7:

```bash
$ git checkout -b apicurio-common-app-components-7
```

After you're happy with your changes and all unit tests run successfully, commit your changes 
on your topic branch. Then it's time to check for and pull any recent changes that were made in
the official repository since you created your branch:

```bash
$ git checkout master         # switches to the 'master' branch
$ git pull upstream master    # fetches all 'upstream' changes and merges 'upstream/master' onto your 'master' branch
$ git checkout apicurio-common-app-components-7  # switches to your topic branch
$ git rebase master           # reapplies your changes on top of the latest in master
                              # (i.e., the latest from master will be the new base for your changes)
```

If the pull grabbed a lot of changes, you should rerun the tests to make sure your changes are 
still good.  You should then push your changes to your fork, and then 
[generate a pull-request](http://help.github.com/pull-requests/) to submit your contribution:

```bash
$ git push origin apicurio-common-app-components-7         # pushes your topic branch into your public fork
```
