package org.example.data_structures;

import com.google.common.collect.ImmutableSet;
import com.google.common.flogger.FluentLogger;
import com.google.common.truth.Truth;
import org.junit.Test;

import java.util.function.Function;
import java.util.logging.Level;


public class MatrixTest {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @Test
    public void stringToMatrixTest() {
        record TestCase(String seed, String delimiter, Function<Character, Integer> characterFunction,
                        Matrix<Integer> expected) {
        }
        ImmutableSet<TestCase> cases = ImmutableSet.<TestCase>builder()
                .add(new TestCase("12|23", "\\|", n -> n - '0',
                        new Matrix<>(new Dimension(2, 2), c -> c.y() + c.x() + 1)))
                .add(new TestCase("12|34", "\\|", n -> n - '0',
                        new Matrix<>(new Dimension(2, 2), c -> c.y() * 2 + c.x() + 1)))
                .add(new TestCase("13|24", "\\|", n -> n - '0',
                        new Matrix<>(new Dimension(2, 2), c -> c.y() + c.x() * 2 + 1)))
                .add(new TestCase("123|234|345", "\\|", n -> n - '0',
                        new Matrix<>(new Dimension(3, 3), c -> c.y() + c.x() + 1)))
                .add(new TestCase("123|234", "\\|", n -> n - '0',
                        new Matrix<>(new Dimension(3, 2), c -> c.y() + c.x() + 1)))
                .add(new TestCase("12|23|34", "\\|", n -> n - '0',
                        new Matrix<>(new Dimension(2, 3), c -> c.y() + c.x() + 1)))
                .build();

        cases.forEach(testCase -> Truth.assertThat(MatrixFactory.stringToMatrix(testCase.seed, testCase.delimiter, testCase.characterFunction))
                .isEqualTo(testCase.expected));
    }

    @Test
    public void getItemAtTest() {
        record TestCase(Matrix<Character> seedMatrix, Coordinate pointer, char expected) {
        }
        ImmutableSet<TestCase> cases = ImmutableSet.<TestCase>builder()
                .add(new TestCase(MatrixFactory.stringToMatrix("123|456|789", "\\|", (character -> character)),
                        new Coordinate(1, 2), '8'))
                .add(new TestCase(MatrixFactory.stringToMatrix("123|456|789", "\\|", (character -> character)),
                        new Coordinate(1, 0), '2'))
                .add(new TestCase(MatrixFactory.stringToMatrix("123|456|789", "\\|", (character -> character)),
                        new Coordinate(2, 1), '6'))
                .add(new TestCase(MatrixFactory.stringToMatrix("123|456|789", "\\|", (character -> character)),
                        new Coordinate(1, 1), '5'))
                .add(new TestCase(MatrixFactory.stringToMatrix("12|45|78", "\\|", (character -> character)),
                        new Coordinate(1, 1), '5'))
                .add(new TestCase(MatrixFactory.stringToMatrix("123|456", "\\|", (character -> character)),
                        new Coordinate(1, 1), '5')).build();
        cases.forEach(testCase -> {
            System.out.println(testCase);
            Truth.assertThat(testCase.seedMatrix.getItemAtCoordinate(testCase.pointer))
                    .isEqualTo(testCase.expected);
        });
    }

    @Test
    public void factoryTest() {
        Level loggingLevel = Level.FINE;

        record TestCase(String seed, Function<Character, Foo> converter, Matrix<Foo> output) {
        }

        ImmutableSet<TestCase> cases = ImmutableSet.<TestCase>builder().add(new TestCase("""
                        XXX
                        XXX
                        XXX""", Foo::fromChar, new Matrix<>(new Dimension(3, 3), coordinate -> Foo.X))).add(new TestCase("""
                        OOO
                        OOO
                        OOO""", Foo::fromChar, new Matrix<>(new Dimension(3, 3), coordinate -> Foo.O)))

                .add(new TestCase("""
                        OOX
                        OOO
                        OOO""", Foo::fromChar, new Matrix<>(new Dimension(3, 3), coordinate -> coordinate.equals(new Coordinate(2, 0)) ? Foo.X : Foo.O))).add(new TestCase("""
                        OOO
                        OOO
                        XOO""", Foo::fromChar, new Matrix<>(new Dimension(3, 3), coordinate -> coordinate.equals(new Coordinate(0, 2)) ? Foo.X : Foo.O))).add(new TestCase("""
                        XOO
                        OOO
                        OOO""", Foo::fromChar, new Matrix<>(new Dimension(3, 3), coordinate -> coordinate.equals(new Coordinate(0, 0)) ? Foo.X : Foo.O)))

                .add(new TestCase("""
                        XOO
                        OOO
                        OOX""", Foo::fromChar, new Matrix<>(new Dimension(3, 3), coordinate -> coordinate.equals(new Coordinate(0, 0)) || coordinate.equals(new Coordinate(2, 2)) ? Foo.X : Foo.O))).build();

        for (TestCase testCase : cases) {
            Matrix<Foo> actual = MatrixFactory.stringToMatrix(testCase.seed, "\n", testCase.converter);
            logger.at(loggingLevel).log("ACTUAL: %s | EXPECTED %s", actual.toString(), testCase.output.toString());
            Truth.assertThat(actual).isEqualTo(testCase.output);
        }
    }

    private enum Foo {
        X, O;

        public static Foo fromChar(char character) {
            return switch (character) {
                case 'X' -> X;
                case 'O' -> O;
                default -> null;
            };
        }

        public boolean toBoolean() {
            return this == X;
        }

        @Override
        public String toString() {
            return switch (this) {
                case O -> "O";
                case X -> "X";
            };
        }
    }

}
