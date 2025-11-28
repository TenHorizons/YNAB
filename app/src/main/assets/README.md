# Default Budget Structure Configuration

## Overview

The `default_budget_structure.json` file defines the default budget categories and items that are automatically created when a new user registers.

## File Location

`app/src/main/assets/default_budget_structure.json`

## Format

```json
{
  "categories": [
    {
      "name": "Category Name",
      "items": [
        "Budget Item 1",
        "Budget Item 2"
      ]
    }
  ]
}
```

## Modifying the Structure

### Adding a New Category

Add a new object to the `categories` array:

```json
{
  "name": "New Category",
  "items": [
    "Item 1",
    "Item 2"
  ]
}
```

### Adding Items to Existing Category

Add strings to the `items` array of the desired category:

```json
{
  "name": "Immediate Obligations",
  "items": [
    "Groceries",
    "Internet",
    "New Item Here"  // <-- Add here
  ]
}
```

### Removing Categories or Items

Simply delete the corresponding object or string from the JSON file.

## Important Notes

1. **Order Matters**: Categories and items will be displayed in the order they appear in the JSON file
2. **Unique Names**: Category names and item names should be unique
3. **Validation**: The app includes a fallback structure in case the JSON file is invalid or missing
4. **Testing**: After modifying, test by creating a new user account to verify the structure loads correctly

## Fallback Behavior

If the JSON file cannot be loaded (corrupted, missing, or invalid), the app will use a hardcoded fallback structure defined in `DefaultBudgetStructureLoader.kt`.

## Future Enhancements

Consider adding:
- Position/order fields for explicit ordering
- Category colors or icons
- Default assigned amounts for items
- Category descriptions
- Item goals or targets
