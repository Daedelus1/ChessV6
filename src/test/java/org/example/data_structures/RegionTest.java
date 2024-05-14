package org.example.data_structures;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.truth.Truth;
import org.junit.jupiter.api.Test;

class RegionTest {

    @Test
    void containsTest() {
        record TestCase(Region region, ImmutableMap<Coordinate, Boolean> testPoints) {
        }
        ImmutableSet<TestCase> cases = ImmutableSet.<TestCase>builder()
                .add(new TestCase(Region.newRegion(1, 1, 2, 2), ImmutableMap.<Coordinate, Boolean>builder()
                        .put(new Coordinate(0, 0), false)
                        .put(new Coordinate(1, 0), false)
                        .put(new Coordinate(0, 1), false)
                        .put(new Coordinate(1, 1), true)
                        .put(new Coordinate(2, 0), false)
                        .put(new Coordinate(3, 0), false)
                        .put(new Coordinate(3, 1), false)
                        .put(new Coordinate(2, 1), true)
                        .put(new Coordinate(2, 2), true)
                        .put(new Coordinate(3, 2), false)
                        .put(new Coordinate(2, 3), false)
                        .put(new Coordinate(3, 3), false)
                        .build()))
                .build();
        cases.forEach(testCase -> testCase.testPoints.forEach((point, out) -> Truth.assertThat(testCase.region.contains(point))
                .isEqualTo(out)));
    }

    @Test
    void allCoordinatesInRegionTest() {

    }
}