# Functional Calculator

A modern, sleek GUI calculator built with Java Swing featuring a glossy button design and smooth user experience.

## Description
This is a fully functional desktop calculator application with a beautiful graphical interface. It features custom-styled rounded buttons with gradient effects, a light green display panel, and supports basic arithmetic operations with high precision using BigDecimal for accurate calculations.

## Features
-  **Modern GUI Design** - Glossy rounded buttons with gradient colors
-  **Custom Styling** - Dark theme with color-coded operator buttons
-  **High Precision Math** - Uses BigDecimal for accurate decimal calculations
-  **Interactive Display** - Shows both current value and operation history
-  **Backspace Function** - Erase button to correct mistakes
-  **Error Handling** - Division by zero protection with user alerts

## Operations Supported
- Addition (+)
- Subtraction (−)
- Multiplication (×)
- Division (÷)
- Decimal point operations

## Requirements
- Java JDK 8 or higher
- Java Swing (included in JDK)

## How to Compile
```bash
javac FunctionalCalculator.java
```

## How to Run
```bash
java FunctionalCalculator
```

## How to Use
1. Click number buttons to enter values
2. Click an operator button (+, −, ×, ÷)
3. Enter the second number
4. Press `=` to see the result
5. Use the ⌫ (backspace) button to delete the last digit

## UI Components
- **Light Green Display Panel** - Shows current input and expression history
- **Color-Coded Buttons**:
  - Gray buttons for numbers (0-9) and decimal point
  - Teal buttons for operators (+, −, ×, ÷)
  - Red equals button (=)
  - Backspace button (⌫) for corrections

## Technical Details
- Built with Java Swing for cross-platform compatibility
- Custom `RoundedGlossyButton` class for stylized buttons
- BigDecimal implementation prevents floating-point errors
- Responsive 340x480 window size

## Author
Maira Lorraine Domaog

## License
MIT License
