import java.util.Arrays;
import java.util.Scanner;

// Обработка исключений
class NumbersException extends Exception {
    public NumbersException(String description) {
        super(description);
    }
}

// Типы чисел
enum NumbersType {
    ARABIC, ROMANS
}

public class Main {
    // Преобразование строки в число
    private static int convertToInt(String number) throws NumbersException {
        int num;
        try {
            num = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new NumbersException("т.к. используются одновременно разные системы счисления");
        }
        return num;
    }

    // Математические операции
    private static String mathOperation(String[] numbers, String sign) throws NumbersException {
        int result;
        int firstNum = convertToInt(numbers[0]);
        int secondNum = convertToInt(numbers[1]);
        if (firstNum < 1 || firstNum > 10 || secondNum < 1 || secondNum > 10) {
            throw new NumbersException("На вход принимаются числа от 1 до 10");
        }

        result = switch (sign) {
            case "+" -> firstNum + secondNum;
            case "-" -> firstNum - secondNum;
            case "*" -> firstNum * secondNum;
            case "/" -> firstNum / secondNum;
            default -> throw new NumbersException("Такого знака математической операции нет, попробуйте (+, -, /, *)");
        };
        return String.valueOf(result);
    }

    // Преобразование числа в выбранный тип
    private static String convertTo(String number, NumbersType numType) throws NumbersException {
        int[] arabicNumbers = {100, 90, 80, 70, 60, 50, 40, 30, 20, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        String[] romanNumbers = {"C", "XC", "LXXX", "LXX", "LX", "L", "XL", "XXX", "XX", "X", "IX", "VIII", "VII", "VI", "V", "IV", "III", "II", "I"};
        StringBuilder result = new StringBuilder();

        if (numType == NumbersType.ROMANS) {
            int num = convertToInt(number);
            if (num < 0) {
                throw new NumbersException("т.к. в римской системе нет отрицательных чисел");
            } else if (num == 0) {
                throw new NumbersException("т.к. в римской системе нет нуля");
            }

            for (int i = 0; num != 0; i++) {
                if (arabicNumbers[i] <= num) {
                    result.append(romanNumbers[i]);
                    num -= arabicNumbers[i];
                }
            }
        } else {
            if (!Arrays.asList(romanNumbers).contains(number)) {
                throw new NumbersException("На вход принимаются числа от I до X");
            }
            for (int i = 0; i < romanNumbers.length; i++) {
                if (number.matches(romanNumbers[i])) {
                    result = new StringBuilder(String.valueOf(arabicNumbers[i]));
                }
            }
        }
        return result.toString();
    }

    // Проверка строки на тип числа
    private static boolean identifyTypeNumber(String[] numbers) {
        return numbers[0].matches("[0-9]+") || numbers[1].matches("[0-9]+");
    }

    public static String calc(String input) throws NumbersException {
        String[] strArray = input.split(" ");
        if (strArray.length < 3 || input.isEmpty()) {
            throw new NumbersException("т.к. строка не является математической операцией");
        } else if (strArray.length > 3) {
            throw new NumbersException("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }
        String sign = strArray[1];
        String[] arrayOfNumbers = {strArray[0], strArray[2]};
        boolean isArabic = identifyTypeNumber(arrayOfNumbers);
        String results;

        if (isArabic) {
            results = mathOperation(arrayOfNumbers, sign);
        } else {
            String[] convertedNumbers = new String[2];
            for (int i = 0; i < arrayOfNumbers.length; i++) {
                convertedNumbers[i] = convertTo(arrayOfNumbers[i], NumbersType.ARABIC);
            }
            String afterMath = mathOperation(convertedNumbers, sign);
            results = convertTo(afterMath, NumbersType.ROMANS);
        }
        return results;
    }

    public static void main(String[] args) throws NumbersException {
        System.out.println("Введите пример");
        Scanner scanner = new Scanner(System.in);
        String result = calc(scanner.nextLine());
        System.out.println(result);
    }
}