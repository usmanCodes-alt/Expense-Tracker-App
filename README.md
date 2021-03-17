# Expense-Tracker-App
An Android App that will help user track and manager their expenses.

## Features
User of this Application would have access to the following features.
1. Add a new Expense
2. Edit an Expense
3. Delete an Expense
4. Read all Expenses in a `RecyclerView`
    * Every `ViewHolder` in `RecyclerView` has a menu option on its left to delete that respective expense
5. Add a Budget
6. Remove Budget

Total Expenses and current user Budget are shown at the top of the `MainActivity`.
User has two options to `Add` or `Delete` budget.
Whenever user choses to `Add` Budget, `BottomSheetFragment` pops up with only one input field that is the Budget field.
`MainActivity` comprise of a `FloatingActionButton` at the bottom left corner. When clicked a new activity `AddExpense`
is launched with three input fields.
1. `title`
2. `Amount`
3. `Category`
    * Nice to have
    * Not Important
    * Must Have

## Database Architecture
`RoomDatabase` has been implemented in this project.
Respective `DAO's` are developed along with respective `Repositories` for `Budget` and `Expenses`.
* `BudgetDao`
* `ExpenseDao`
* `BudgetRepository`
* `ExpenseRepository`

Application only uses one view model that is the `ApplicationViewModel` which comprise of `public static methods` which invoke the
APIs provided by respective repository of each entity.
`ApplicationViewModel` is used throughout the app to perform all database related operations i.e. all `CRUD` on both Entities.
