Writing my own YNAB so I don't need to pay subscription.
Using Kotlin with Room.


Ready features include:
- Simple register and login with Room
- Splash(Loading) screen to load user data/generate new user data
- Settings page with delete account button (to clean up accounts if I do too much testing)
- Top app bar and bottom navigation bar


Planned features include
1.  Accounts page for bank accounts, with add/delete/update account functionality
2.  Add Transaction page
3.  Transaction page under accounts for transactions per account, with  delete/update/filter/sort transaction functionality
4.  Budget page with top bar month scroller, with updates from transactions, and ability to assign surplus income (default to only 1 budget).
5.  Budget Item page with budget item details.
6.  Goal page to set goal per budget item, with change reflected across relevant budget items across months. (Goal should be decoupled from budget item)


Far-future features include:
- Enhance splash screen to include a simple walkthrough on how to use the app
- Cloud storage (maybe Firebase if webage friendly)
- Webpage
