FreePublisher
=============

My plugin for Freenet.

Needs jdom which is bundled to final jar with plugin (this is required by Freenet).

One day, this will allow you to:


 * Publish things to Freenet under so called "Identity"
 * Change things you published (and keeping history of changes?).
 * Subscribe to other people and see their changes.
and maybe:
 * Fork other people's things and let them merge your changes.

Event table? Event?
-------------------

Every person (or every "Identity") has an EventTable, stored in USK. EventTable contains information about person's activities like creating new document, updating one, forking etc. Such activities are called Events.

Updates about followed (subscribed) people are obtained by fetching their EventTables.

Wheter EventTable is created (pushed to Freenet) upon Identity creation or when publishing first thing is undecided yet.

Tasks?
------

> Warning - this is not _exactly_ true anymore, as I'm trying to create some sort of MVC pattern.
> Business logic is in models package, controllers (which are also views, because of lack of templating
> system) are in web package.

Accessing content on Freenet takes a while and should be done in background, informing user about the progress via user interface. 

Tasks are pushed to appropriate queues in TaskManager, which decides what should run first and what will be ran parallel. For example, publishing thing should take precedence over feeds update.