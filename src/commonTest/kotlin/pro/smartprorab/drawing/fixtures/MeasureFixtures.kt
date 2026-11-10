package pro.smartprorab.drawing.fixtures

/**
 * Реальные чертежи из `rv_measure` (выгрузка 23.10.2026, #3).
 * Дубли геометрии убраны. Ожидаемые значения посчитаны по правилу из #2:
 * координаты в целых мм, площадь — `(abs(twiceAreaMm2) + 100_000) / 200_000` в десятых м²,
 * периметр — сумма сторон, half-up до десятых м.
 */
enum class FixtureTag { RANDOM, BOUNDARY, REVERSE, DEGENERATE, EMPTY }

data class ExpectedContour(
    /**
     * Знак `twiceSignedAreaMm2()` в доменных координатах (x вправо, y вверх): 1, -1 или 0.
     * Нормализованный обход web — по часовой, т. е. -1.
     */
    val orientationSign: Int,
    /** Площадь по новому правилу, десятые м². */
    val areaTenthsM2: Long,
    /** Периметр по новому правилу, десятые м. */
    val perimeterTenthsM: Long,
    /** Площадь, сохранённая в JSON, десятые м². */
    val storedAreaTenthsM2: Long,
    /** Периметр, сохранённый в JSON, десятые м. */
    val storedPerimeterTenthsM: Long,
    /** ≥ 3 вершин и ненулевая площадь: такой контур можно сохранить. */
    val savable: Boolean,
)

data class MeasureFixture(
    val tag: FixtureTag,
    val roomId: Int,
    /** Тело `MeasureData` как его отдаёт сервер (без `updated_at` и `ver`). */
    val json: String,
    val contours: List<ExpectedContour>,
    val note: String,
)

object MeasureFixtures {
    val all: List<MeasureFixture> = listOf(
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1100545,
            json = """{"room_id":1100545,"measure":[{"area":11.7,"points":[[318.1,411.0],[601.1,411.0],[601.1,424.0],[701.1,424.0],[701.1,721.0],[318.1,721.0]],"diagonals":[],"perimeter":13.9}],"area":11.7,"perimeter":13.9}""",
            contours = listOf(
                ExpectedContour(-1, 117, 139, 117, 139, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1101028,
            json = """{"room_id":1101028,"measure":[{"area":1.6,"points":[[326.1,483.0],[435.1,483.0],[435.1,634.0],[326.1,634.0]],"diagonals":[],"perimeter":5.2}],"area":1.6,"perimeter":5.2}""",
            contours = listOf(
                ExpectedContour(-1, 16, 52, 16, 52, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1101047,
            json = """{"room_id":1101047,"measure":[{"area":1.8,"points":[[475.1,698.5],[633.1,698.5],[633.1,810.5],[475.1,810.5]],"diagonals":[],"perimeter":5.4}],"area":1.8,"perimeter":5.4}""",
            contours = listOf(
                ExpectedContour(-1, 18, 54, 18, 54, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1101304,
            json = """{"room_id":1101304,"measure":[{"area":6.1,"points":[[350.0,406.0],[669.0,406.0],[669.0,373.0],[769.0,373.0],[769.0,649.0],[569.0,649.0],[569.0,525.0],[629.0,525.0],[629.0,465.0],[349.0,465.0]],"diagonals":[],"perimeter":15.1}],"area":6.1,"perimeter":15.1}""",
            contours = listOf(
                ExpectedContour(-1, 61, 151, 61, 151, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1101503,
            json = """{"room_id":1101503,"measure":[{"area":3.8,"points":[[474.0,355.0],[489.0,355.0],[489.0,285.0],[667.0,285.0],[667.0,322.0],[734.0,322.0],[734.0,443.0],[471.0,443.0]],"diagonals":[],"perimeter":8.4}],"area":3.8,"perimeter":8.4}""",
            contours = listOf(
                ExpectedContour(-1, 38, 84, 38, 84, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1102309,
            json = """{"room_id":1102309,"measure":[{"area":12.2,"points":[[375.0,390.0],[766.0,390.0],[766.0,702.0],[375.0,702.0]],"diagonals":[],"perimeter":14.1}],"area":12.2,"perimeter":14.1}""",
            contours = listOf(
                ExpectedContour(-1, 122, 141, 122, 141, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1102724,
            json = """{"room_id":1102724,"measure":[{"area":9.9,"points":[[384.0,330.0],[693.0,330.0],[693.0,651.0],[384.0,651.0]],"diagonals":[],"perimeter":12.6}],"area":9.9,"perimeter":12.6}""",
            contours = listOf(
                ExpectedContour(-1, 99, 126, 99, 126, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1102733,
            json = """{"room_id":1102733,"measure":[{"area":8.1,"points":[[367.0,479.0],[614.0,479.0],[614.0,623.0],[856.0,623.0],[856.0,730.0],[417.0,730.0],[417.0,589.0],[367.0,589.0]],"diagonals":[],"perimeter":14.8}],"area":8.1,"perimeter":14.8}""",
            contours = listOf(
                ExpectedContour(-1, 81, 148, 81, 148, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1103012,
            json = """{"room_id":1103012,"measure":[{"area":15.9,"points":[[384.0,474.0],[714.0,474.0],[714.0,956.0],[384.0,956.0]],"diagonals":[],"perimeter":16.2}],"area":15.9,"perimeter":16.2}""",
            contours = listOf(
                ExpectedContour(-1, 159, 162, 159, 162, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1104269,
            json = """{"room_id":1104269,"measure":[{"area":3.2,"points":[[327.0,389.0],[657.0,389.0],[657.0,486.0],[327.0,486.0]],"diagonals":[],"perimeter":8.5}],"area":3.2,"perimeter":8.5}""",
            contours = listOf(
                ExpectedContour(-1, 32, 85, 32, 85, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1104594,
            json = """{"room_id":1104594,"measure":[{"area":6.4,"points":[[344.0,379.0],[742.0,379.0],[742.0,539.0],[344.0,539.0]],"diagonals":[],"perimeter":11.2}],"area":6.4,"perimeter":11.2}""",
            contours = listOf(
                ExpectedContour(-1, 64, 112, 64, 112, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1104612,
            json = """{"room_id":1104612,"measure":[{"area":6.4,"points":[[346.0,394.0],[681.0,394.0],[681.0,214.0],[791.0,214.0],[791.0,492.0],[342.0,492.0]],"diagonals":[],"perimeter":14.5}],"area":6.4,"perimeter":14.5}""",
            contours = listOf(
                ExpectedContour(-1, 64, 145, 64, 145, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1104673,
            json = """{"room_id":1104673,"measure":[{"area":5.1,"points":[[399.0,472.0],[680.0,472.0],[680.0,655.0],[399.0,655.0]],"diagonals":[],"perimeter":9.3}],"area":5.1,"perimeter":9.3}""",
            contours = listOf(
                ExpectedContour(-1, 51, 93, 51, 93, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1104707,
            json = """{"room_id":1104707,"measure":[{"area":16.5,"points":[[403.0,495.0],[687.0,495.0],[807.2,615.2],[807.2,922.2],[405.2,922.2]],"diagonals":[],"perimeter":15.9}],"area":16.5,"perimeter":15.9}""",
            contours = listOf(
                ExpectedContour(-1, 165, 159, 165, 159, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1104984,
            json = """{"room_id":1104984,"measure":[{"area":15.0,"points":[[385.0,622.0],[757.0,622.0],[757.0,1024.0],[385.0,1024.0]],"diagonals":[],"perimeter":15.5}],"area":15,"perimeter":15.5}""",
            contours = listOf(
                ExpectedContour(-1, 150, 155, 150, 155, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1105218,
            json = """{"room_id":1105218,"measure":[{"area":2.9,"points":[[293.0,692.0],[433.0,692.0],[433.0,898.0],[293.0,898.0]],"diagonals":[],"perimeter":6.9}],"area":2.9,"perimeter":6.9}""",
            contours = listOf(
                ExpectedContour(-1, 29, 69, 29, 69, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1105332,
            json = """{"room_id":1105332,"measure":[{"area":2.8,"points":[[343.0,427.0],[534.0,427.0],[534.0,575.0],[343.0,575.0]],"diagonals":[],"perimeter":6.8}],"area":2.8,"perimeter":6.8}""",
            contours = listOf(
                ExpectedContour(-1, 28, 68, 28, 68, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1105474,
            json = """{"room_id":1105474,"measure":[{"area":16.5,"points":[[388.0,468.0],[725.0,468.0],[725.0,959.0],[388.0,959.0]],"diagonals":[],"perimeter":16.6}],"area":16.5,"perimeter":16.6}""",
            contours = listOf(
                ExpectedContour(-1, 165, 166, 165, 166, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1105558,
            json = """{"room_id":1105558,"measure":[{"area":2.0,"points":[[374.0,421.0],[494.0,421.0],[494.0,591.0],[374.0,591.0]],"diagonals":[],"perimeter":5.8}],"area":2,"perimeter":5.8}""",
            contours = listOf(
                ExpectedContour(-1, 20, 58, 20, 58, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1105782,
            json = """{"room_id":1105782,"measure":[{"area":18.8,"points":[[439.1,579.0],[995.1,579.0],[995.1,917.0],[439.1,917.0]],"diagonals":[],"perimeter":17.9}],"area":18.8,"perimeter":17.9}""",
            contours = listOf(
                ExpectedContour(-1, 188, 179, 188, 179, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1105849,
            json = """{"room_id":1105849,"measure":[{"area":3.4,"points":[[358.1,721.0],[529.1,721.0],[529.1,922.0],[358.1,922.0]],"diagonals":[],"perimeter":7.4}],"area":3.4,"perimeter":7.4}""",
            contours = listOf(
                ExpectedContour(-1, 34, 74, 34, 74, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1105975,
            json = """{"room_id":1105975,"measure":[{"area":10.6,"points":[[265.1,241.0],[574.1,241.0],[574.1,584.0],[265.1,584.0]],"diagonals":[],"perimeter":13.0}],"area":10.6,"perimeter":13}""",
            contours = listOf(
                ExpectedContour(-1, 106, 130, 106, 130, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1106149,
            json = """{"room_id":1106149,"measure":[{"area":18.6,"points":[[395.1,536.0],[730.1,536.0],[730.1,1092.0],[395.1,1092.0]],"diagonals":[],"perimeter":17.8}],"area":18.6,"perimeter":17.8}""",
            contours = listOf(
                ExpectedContour(-1, 186, 178, 186, 178, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1106309,
            json = """{"room_id":1106309,"measure":[{"area":18.8,"points":[[231.1,541.0],[787.1,541.0],[787.1,879.0],[231.1,879.0]],"diagonals":[],"perimeter":17.9}],"area":18.8,"perimeter":17.9}""",
            contours = listOf(
                ExpectedContour(-1, 188, 179, 188, 179, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1106443,
            json = """{"room_id":1106443,"measure":[{"area":18.1,"points":[[433.1,994.0],[989.1,994.0],[989.1,1319.0],[433.1,1319.0]],"diagonals":[],"perimeter":17.6}],"area":18.1,"perimeter":17.6}""",
            contours = listOf(
                ExpectedContour(-1, 181, 176, 181, 176, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1106455,
            json = """{"room_id":1106455,"measure":[{"area":16.8,"points":[[346.1,723.0],[838.1,723.0],[838.1,1064.0],[346.1,1064.0]],"diagonals":[],"perimeter":16.7}],"area":16.8,"perimeter":16.7}""",
            contours = listOf(
                ExpectedContour(-1, 168, 167, 168, 167, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1106861,
            json = """{"room_id":1106861,"measure":[{"area":3.8,"points":[[383.0,503.0],[601.0,503.0],[601.0,676.0],[383.0,676.0]],"diagonals":[],"perimeter":7.8}],"area":3.8,"perimeter":7.8}""",
            contours = listOf(
                ExpectedContour(-1, 38, 78, 38, 78, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1106942,
            json = """{"room_id":1106942,"measure":[{"area":5.1,"points":[[438.0,462.0],[652.0,462.0],[652.0,703.0],[448.0,703.0],[448.0,608.0],[435.0,608.0]],"diagonals":[],"perimeter":9.1}],"area":5.1,"perimeter":9.1}""",
            contours = listOf(
                ExpectedContour(-1, 51, 91, 51, 91, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1107002,
            json = """{"room_id":1107002,"measure":[{"area":4.6,"points":[[210.0,328.9],[260.0,328.9],[260.0,241.9],[491.0,241.9],[491.0,412.9],[341.0,412.9],[341.0,428.9],[209.0,428.9]],"diagonals":[],"perimeter":9.4}],"area":4.6,"perimeter":9.4}""",
            contours = listOf(
                ExpectedContour(-1, 46, 94, 46, 94, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1107188,
            json = """{"room_id":1107188,"measure":[{"area":11.9,"points":[[627.0,510.5],[904.0,510.5],[904.0,522.5],[957.0,522.5],[957.0,871.5],[626.0,871.5]],"diagonals":[],"perimeter":13.8}],"area":11.9,"perimeter":13.8}""",
            contours = listOf(
                ExpectedContour(-1, 119, 138, 119, 138, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1107264,
            json = """{"room_id":1107264,"measure":[{"area":10.3,"points":[[653.0,522.5],[739.0,522.5],[739.0,562.5],[1125.0,561.0],[1122.0,690.5],[1197.0,690.5],[1197.0,819.5],[981.0,819.5],[981.0,807.5],[903.0,807.5],[903.0,701.5],[704.0,704.5],[704.0,689.5],[654.0,689.5]],"diagonals":[],"perimeter":16.9}],"area":10.3,"perimeter":16.9}""",
            contours = listOf(
                ExpectedContour(-1, 103, 169, 103, 169, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1107924,
            json = """{"room_id":1107924,"measure":[{"area":3.8,"points":[[370.0,334.0],[452.0,334.0],[452.0,305.0],[542.0,305.0],[542.0,542.0],[370.0,542.0]],"diagonals":[],"perimeter":8.2}],"area":3.8,"perimeter":8.2}""",
            contours = listOf(
                ExpectedContour(-1, 38, 82, 38, 82, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1108013,
            json = """{"room_id":1108013,"measure":[{"area":2.5,"points":[[331.0,351.0],[408.0,351.0],[408.0,326.0],[489.0,326.0],[489.0,497.0],[330.0,496.0]],"diagonals":[],"perimeter":6.6}],"area":2.5,"perimeter":6.6}""",
            contours = listOf(
                ExpectedContour(-1, 25, 66, 25, 66, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1108172,
            json = """{"room_id":1108172,"measure":[{"area":9.1,"points":[[321.0,417.0],[659.0,417.0],[659.0,571.0],[416.0,571.0],[416.0,974.0],[317.0,974.0]],"diagonals":[],"perimeter":17.9}],"area":9.1,"perimeter":17.9}""",
            contours = listOf(
                ExpectedContour(-1, 91, 179, 91, 179, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1108367,
            json = """{"room_id":1108367,"measure":[{"area":9.0,"points":[[402.0,564.0],[660.0,564.0],[660.0,913.0],[402.0,913.0]],"diagonals":[],"perimeter":12.1}],"area":9,"perimeter":12.1}""",
            contours = listOf(
                ExpectedContour(-1, 90, 121, 90, 121, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1108492,
            json = """{"room_id":1108492,"measure":[{"area":17.9,"points":[[340.0,303.0],[646.0,303.0],[646.0,680.0],[477.0,680.0],[477.0,690.0],[646.0,690.0],[646.0,838.0],[669.0,838.0],[669.0,891.0],[342.0,891.0]],"diagonals":[],"perimeter":21.7}],"area":17.9,"perimeter":21.7}""",
            contours = listOf(
                ExpectedContour(-1, 179, 217, 179, 217, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1108576,
            json = """{"room_id":1108576,"measure":[{"area":20.9,"points":[[363.1,419.0],[751.1,419.0],[751.1,919.0],[695.1,919.0],[615.9,998.2],[499.9,998.2],[420.7,919.0],[364.7,919.0]],"diagonals":[],"perimeter":18.4}],"area":20.9,"perimeter":18.4}""",
            contours = listOf(
                ExpectedContour(-1, 209, 184, 209, 184, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1109161,
            json = """{"room_id":1109161,"measure":[{"area":11.0,"points":[[495.1,456.0],[720.1,456.0],[720.1,394.0],[933.1,394.0],[933.1,695.0],[640.1,695.0],[640.1,638.0],[495.1,638.0]],"diagonals":[],"perimeter":14.8}],"area":11,"perimeter":14.8}""",
            contours = listOf(
                ExpectedContour(-1, 110, 148, 110, 148, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1109808,
            json = """{"room_id":1109808,"measure":[{"area":11.4,"points":[[329.0,156.5],[669.0,156.5],[669.0,491.5],[329.0,491.5]],"diagonals":[],"perimeter":13.5}],"area":11.4,"perimeter":13.5}""",
            contours = listOf(
                ExpectedContour(-1, 114, 135, 114, 135, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1109889,
            json = """{"room_id":1109889,"measure":[{"area":19.5,"points":[[384.0,431.5],[669.0,431.5],[669.0,454.5],[819.0,454.5],[819.0,879.5],[695.0,879.5],[695.0,891.5],[384.0,891.5]],"diagonals":[],"perimeter":17.9}],"area":19.5,"perimeter":17.9}""",
            contours = listOf(
                ExpectedContour(-1, 195, 179, 195, 179, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1110130,
            json = """{"room_id":1110130,"measure":[{"area":11.0,"points":[[318.0,509.5],[641.0,509.5],[641.0,849.5],[318.0,849.5]],"diagonals":[],"perimeter":13.3}],"area":11,"perimeter":13.3}""",
            contours = listOf(
                ExpectedContour(-1, 110, 133, 110, 133, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1110192,
            json = """{"room_id":1110192,"measure":[{"area":2.7,"points":[[209.9,128.8],[392.9,128.8],[392.9,277.8],[209.9,277.8]],"diagonals":[],"perimeter":6.6}],"area":2.7,"perimeter":6.6}""",
            contours = listOf(
                ExpectedContour(-1, 27, 66, 27, 66, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1110587,
            json = """{"room_id":1110587,"measure":[{"area":3.3,"points":[[387.0,328.8],[535.0,328.8],[535.0,392.8],[566.0,392.8],[566.0,525.8],[385.0,525.8]],"diagonals":[],"perimeter":7.5}],"area":3.3,"perimeter":7.5}""",
            contours = listOf(
                ExpectedContour(-1, 33, 75, 33, 75, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1110675,
            json = """{"room_id":1110675,"measure":[{"area":16.2,"points":[[357.0,427.5],[874.0,427.5],[874.0,709.5],[548.0,709.5],[548.0,794.5],[356.0,794.5]],"diagonals":[],"perimeter":17.7}],"area":16.2,"perimeter":17.7}""",
            contours = listOf(
                ExpectedContour(-1, 162, 177, 162, 177, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1110746,
            json = """{"room_id":1110746,"measure":[{"area":16.3,"points":[[1061.0,766.5],[1579.0,766.5],[1579.0,1039.5],[1236.0,1039.5],[1236.0,1160.5],[1061.0,1160.5]],"diagonals":[],"perimeter":18.2}],"area":16.3,"perimeter":18.2}""",
            contours = listOf(
                ExpectedContour(-1, 163, 182, 163, 182, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1110967,
            json = """{"room_id":1110967,"measure":[{"area":11.2,"points":[[1100.0,619.0],[1432.0,619.0],[1432.0,1027.0],[1291.0,1027.0],[1291.0,1001.0],[1168.0,1001.0],[1168.0,735.0],[1101.0,735.0]],"diagonals":[],"perimeter":14.8}],"area":11.2,"perimeter":14.8}""",
            contours = listOf(
                ExpectedContour(-1, 112, 148, 112, 148, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1111217,
            json = """{"room_id":1111217,"measure":[{"area":8.3,"points":[[359.0,325.5],[674.0,325.5],[674.0,590.5],[359.0,590.5]],"diagonals":[],"perimeter":11.6}],"area":8.3,"perimeter":11.6}""",
            contours = listOf(
                ExpectedContour(-1, 83, 116, 83, 116, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1111643,
            json = """{"room_id":1111643,"measure":[{"area":15.3,"points":[[423.0,434.5],[595.0,434.5],[595.0,504.5],[941.0,504.5],[941.0,776.5],[425.0,776.5]],"diagonals":[],"perimeter":17.2}],"area":15.3,"perimeter":17.2}""",
            contours = listOf(
                ExpectedContour(-1, 153, 172, 153, 172, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1112098,
            json = """{"room_id":1112098,"measure":[{"area":3.5,"points":[[364.0,573.5],[539.0,573.5],[539.0,773.5],[364.0,773.5]],"diagonals":[],"perimeter":7.5}],"area":3.5,"perimeter":7.5}""",
            contours = listOf(
                ExpectedContour(-1, 35, 75, 35, 75, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1112393,
            json = """{"room_id":1112393,"measure":[{"area":10.1,"points":[[305.0,453.5],[537.0,453.5],[537.0,543.5],[917.0,543.5],[917.0,729.5],[810.0,729.5],[810.0,652.5],[407.0,649.5],[407.0,708.5],[305.0,708.5]],"diagonals":[],"perimeter":18.9}],"area":10.1,"perimeter":18.9}""",
            contours = listOf(
                ExpectedContour(-1, 101, 189, 101, 189, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1112516,
            json = """{"room_id":1112516,"measure":[{"area":10.1,"points":[[312.0,409.5],[632.0,409.5],[632.0,724.5],[312.0,724.5]],"diagonals":[],"perimeter":12.7}],"area":10.1,"perimeter":12.7}""",
            contours = listOf(
                ExpectedContour(-1, 101, 127, 101, 127, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1112856,
            json = """{"room_id":1112856,"measure":[{"area":15.9,"points":[[366.0,580.5],[546.0,580.5],[546.0,644.5],[887.0,644.5],[887.0,928.5],[369.0,928.5]],"diagonals":[],"perimeter":17.4}],"area":15.9,"perimeter":17.4}""",
            contours = listOf(
                ExpectedContour(-1, 159, 174, 159, 174, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1113010,
            json = """{"room_id":1113010,"measure":[{"area":1.2,"points":[[469.0,394.9],[569.0,394.9],[569.0,510.9],[469.0,510.9]],"diagonals":[],"perimeter":4.3}],"area":1.2,"perimeter":4.3}""",
            contours = listOf(
                ExpectedContour(-1, 12, 43, 12, 43, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1113052,
            json = """{"room_id":1113052,"measure":[{"area":3.5,"points":[[280.6,173.6],[451.6,173.6],[451.6,307.6],[421.6,307.6],[421.6,394.6],[282.6,394.6]],"diagonals":[],"perimeter":7.8}],"area":3.5,"perimeter":7.8}""",
            contours = listOf(
                ExpectedContour(-1, 35, 78, 35, 78, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1113230,
            json = """{"room_id":1113230,"measure":[{"area":3.1,"points":[[396.0,404.5],[515.0,404.5],[515.0,429.5],[573.0,429.5],[573.0,585.5],[396.0,585.5]],"diagonals":[],"perimeter":7.2}],"area":3.1,"perimeter":7.2}""",
            contours = listOf(
                ExpectedContour(-1, 31, 72, 31, 72, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1113255,
            json = """{"room_id":1113255,"measure":[{"area":16.5,"points":[[344.0,344.5],[862.0,344.5],[862.0,629.5],[543.0,629.5],[543.0,715.5],[344.0,715.5]],"diagonals":[],"perimeter":17.8}],"area":16.5,"perimeter":17.8}""",
            contours = listOf(
                ExpectedContour(-1, 165, 178, 165, 178, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1113498,
            json = """{"room_id":1113498,"measure":[{"area":14.0,"points":[[349.0,594.5],[594.0,594.5],[594.0,506.5],[794.0,506.5],[794.0,522.5],[946.0,522.5],[946.0,781.5],[476.0,781.5],[476.0,719.5],[466.0,719.5],[466.0,781.5],[345.0,781.5]],"diagonals":[],"perimeter":18.7}],"area":14,"perimeter":18.7}""",
            contours = listOf(
                ExpectedContour(-1, 140, 187, 140, 187, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1113685,
            json = """{"room_id":1113685,"measure":[{"area":4.5,"points":[[347.0,363.5],[621.0,363.5],[621.0,533.5],[421.0,533.5],[421.0,514.5],[348.0,514.5]],"diagonals":[],"perimeter":8.9}],"area":4.5,"perimeter":8.9}""",
            contours = listOf(
                ExpectedContour(-1, 45, 89, 45, 89, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1113958,
            json = """{"room_id":1113958,"measure":[{"area":3.0,"points":[[336.0,455.5],[519.0,455.5],[519.0,620.5],[336.0,620.5]],"diagonals":[],"perimeter":7.0}],"area":3,"perimeter":7}""",
            contours = listOf(
                ExpectedContour(-1, 30, 70, 30, 70, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1113989,
            json = """{"room_id":1113989,"measure":[{"area":3.4,"points":[[272.0,628.5],[482.0,628.5],[482.0,798.5],[345.0,798.5],[345.0,773.5],[274.0,773.5]],"diagonals":[],"perimeter":7.6}],"area":3.4,"perimeter":7.6}""",
            contours = listOf(
                ExpectedContour(-1, 34, 76, 34, 76, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1114133,
            json = """{"room_id":1114133,"measure":[{"area":13.1,"points":[[362.0,358.5],[557.0,358.5],[557.0,124.5],[729.0,124.5],[729.0,349.5],[662.0,349.5],[662.0,663.5],[361.0,663.5]],"diagonals":[],"perimeter":18.1}],"area":13.1,"perimeter":18.1}""",
            contours = listOf(
                ExpectedContour(-1, 131, 181, 131, 181, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1115118,
            json = """{"room_id":1115118,"measure":[{"area":13.3,"points":[[438.0,436.5],[804.0,436.5],[804.0,799.5],[438.0,799.5]],"diagonals":[],"perimeter":14.6}],"area":13.3,"perimeter":14.6}""",
            contours = listOf(
                ExpectedContour(-1, 133, 146, 133, 146, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1115199,
            json = """{"room_id":1115199,"measure":[{"area":14.6,"points":[[352.0,617.5],[806.0,617.5],[806.0,938.5],[352.0,938.5]],"diagonals":[],"perimeter":15.5}],"area":14.6,"perimeter":15.5}""",
            contours = listOf(
                ExpectedContour(-1, 146, 155, 146, 155, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1115214,
            json = """{"room_id":1115214,"measure":[{"area":15.5,"points":[[329.0,567.5],[392.0,567.5],[392.0,536.5],[675.0,536.5],[675.0,700.5],[520.0,700.5],[520.0,942.5],[722.0,942.5],[722.0,1173.5],[407.0,1173.5],[407.0,693.5],[323.0,693.5]],"diagonals":[],"perimeter":23.8}],"area":15.5,"perimeter":23.8}""",
            contours = listOf(
                ExpectedContour(-1, 155, 238, 155, 238, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1115358,
            json = """{"room_id":1115358,"measure":[{"area":15.2,"points":[[578.0,782.5],[926.0,782.5],[926.0,1218.5],[578.0,1218.5]],"diagonals":[],"perimeter":15.7}],"area":15.2,"perimeter":15.7}""",
            contours = listOf(
                ExpectedContour(-1, 152, 157, 152, 157, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1115432,
            json = """{"room_id":1115432,"measure":[{"area":14.5,"points":[[398.0,447.5],[758.0,447.5],[758.0,851.5],[398.0,851.5]],"diagonals":[],"perimeter":15.3}],"area":14.5,"perimeter":15.3}""",
            contours = listOf(
                ExpectedContour(-1, 145, 153, 145, 153, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1115532,
            json = """{"room_id":1115532,"measure":[{"area":13.7,"points":[[418.0,484.5],[798.0,484.5],[798.0,844.5],[418.0,844.5]],"diagonals":[],"perimeter":14.8}],"area":13.7,"perimeter":14.8}""",
            contours = listOf(
                ExpectedContour(-1, 137, 148, 137, 148, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1116106,
            json = """{"room_id":1116106,"measure":[{"area":10.1,"points":[[392.0,575.0],[542.0,575.0],[542.0,585.0],[722.0,585.0],[722.0,885.0],[392.0,885.0]],"diagonals":[],"perimeter":12.8}],"area":10.1,"perimeter":12.8}""",
            contours = listOf(
                ExpectedContour(-1, 101, 128, 101, 128, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1116219,
            json = """{"room_id":1116219,"measure":[{"area":18.6,"points":[[480.0,410.0],[1047.0,410.0],[1047.0,740.0],[945.0,740.0],[945.0,734.0],[743.0,734.0],[743.0,740.0],[478.0,740.0]],"diagonals":[],"perimeter":18.1}],"area":18.6,"perimeter":18.1}""",
            contours = listOf(
                ExpectedContour(-1, 186, 181, 186, 181, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1116411,
            json = """{"room_id":1116411,"measure":[{"area":16.5,"points":[[433.0,493.0],[637.0,493.0],[637.0,499.0],[838.0,499.0],[838.0,493.0],[940.0,493.0],[940.0,823.0],[838.0,823.0],[838.0,817.0],[637.0,817.0],[637.0,823.0],[433.0,823.0]],"diagonals":[],"perimeter":17.0}],"area":16.5,"perimeter":17}""",
            contours = listOf(
                ExpectedContour(-1, 165, 170, 165, 170, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1116723,
            json = """{"room_id":1116723,"measure":[{"area":4.7,"points":[[485.0,400.0],[716.0,400.0],[716.0,604.0],[485.0,604.0]],"diagonals":[],"perimeter":8.7}],"area":4.7,"perimeter":8.7}""",
            contours = listOf(
                ExpectedContour(-1, 47, 87, 47, 87, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1116786,
            json = """{"room_id":1116786,"measure":[{"area":15.2,"points":[[446.0,309.0],[876.0,309.0],[876.0,667.0],[764.0,667.0],[764.0,657.0],[562.0,657.0],[562.0,667.0],[446.0,667.0]],"diagonals":[],"perimeter":16.0}],"area":15.2,"perimeter":16}""",
            contours = listOf(
                ExpectedContour(-1, 152, 160, 152, 160, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1116918,
            json = """{"room_id":1116918,"measure":[{"area":10.2,"points":[[403.0,647.0],[713.0,647.0],[713.0,977.0],[403.0,977.0]],"diagonals":[],"perimeter":12.8}],"area":10.2,"perimeter":12.8}""",
            contours = listOf(
                ExpectedContour(-1, 102, 128, 102, 128, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1116940,
            json = """{"room_id":1116940,"measure":[{"area":12.5,"points":[[426.0,391.0],[626.0,391.0],[626.0,512.0],[586.0,512.0],[586.0,957.0],[701.0,957.0],[701.0,1085.0],[438.0,1085.0],[438.0,650.0],[427.0,650.0]],"diagonals":[],"perimeter":20.2}],"area":12.5,"perimeter":20.2}""",
            contours = listOf(
                ExpectedContour(-1, 125, 202, 125, 202, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1117057,
            json = """{"room_id":1117057,"measure":[{"area":11.2,"points":[[359.0,409.0],[491.0,409.0],[491.0,419.0],[671.0,419.0],[671.0,774.0],[359.0,774.0]],"diagonals":[],"perimeter":13.5}],"area":11.2,"perimeter":13.5}""",
            contours = listOf(
                ExpectedContour(-1, 112, 135, 112, 135, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1117284,
            json = """{"room_id":1117284,"measure":[{"area":16.5,"points":[[362.0,527.0],[539.0,527.0],[539.0,537.0],[743.0,537.0],[743.0,527.0],[854.0,527.0],[854.0,866.0],[362.0,866.0]],"diagonals":[],"perimeter":16.8}],"area":16.5,"perimeter":16.8}""",
            contours = listOf(
                ExpectedContour(-1, 165, 168, 165, 168, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1117358,
            json = """{"room_id":1117358,"measure":[{"area":10.4,"points":[[493.5,926.3],[820.5,926.3],[820.5,1245.3],[493.5,1245.3]],"diagonals":[],"perimeter":12.9}],"area":10.4,"perimeter":12.9}""",
            contours = listOf(
                ExpectedContour(-1, 104, 129, 104, 129, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1117407,
            json = """{"room_id":1117407,"measure":[{"area":11.2,"points":[[398.0,220.0],[828.0,220.0],[828.0,338.0],[739.0,338.0],[739.0,685.0],[614.0,685.0],[614.0,425.0],[490.0,425.0],[490.0,411.0],[395.0,411.0]],"diagonals":[],"perimeter":17.9}],"area":11.2,"perimeter":17.9}""",
            contours = listOf(
                ExpectedContour(-1, 112, 179, 112, 179, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1117492,
            json = """{"room_id":1117492,"measure":[{"area":14.5,"points":[[415.0,503.0],[856.0,503.0],[856.0,849.0],[476.0,849.0],[476.0,726.0],[414.0,726.0]],"diagonals":[],"perimeter":15.8}],"area":14.5,"perimeter":15.8}""",
            contours = listOf(
                ExpectedContour(-1, 145, 158, 145, 158, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1117800,
            json = """{"room_id":1117800,"measure":[{"area":24.5,"points":[[383.0,515.0],[551.0,515.0],[551.0,456.0],[972.0,456.0],[972.0,743.0],[958.0,743.0],[958.0,893.0],[383.0,893.0]],"diagonals":[],"perimeter":20.5}],"area":24.5,"perimeter":20.5}""",
            contours = listOf(
                ExpectedContour(-1, 245, 205, 245, 205, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1118266,
            json = """{"room_id":1118266,"measure":[{"area":12.7,"points":[[433.0,532.0],[647.0,532.0],[647.0,546.0],[796.0,546.0],[796.0,871.0],[671.0,871.0],[671.0,893.0],[431.0,893.0]],"diagonals":[],"perimeter":14.5}],"area":12.7,"perimeter":14.5}""",
            contours = listOf(
                ExpectedContour(-1, 127, 145, 127, 145, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1118365,
            json = """{"room_id":1118365,"measure":[{"area":3.3,"points":[[383.0,399.0],[538.0,399.0],[538.0,459.0],[568.0,459.0],[568.0,589.0],[383.0,589.0]],"diagonals":[],"perimeter":7.5}],"area":3.3,"perimeter":7.5}""",
            contours = listOf(
                ExpectedContour(-1, 33, 75, 33, 75, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1119557,
            json = """{"room_id":1119557,"measure":[{"area":2.6,"points":[[360.0,397.0],[541.0,397.0],[541.0,538.0],[360.0,538.0]],"diagonals":[],"perimeter":6.4}],"area":2.6,"perimeter":6.4}""",
            contours = listOf(
                ExpectedContour(-1, 26, 64, 26, 64, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1119893,
            json = """{"room_id":1119893,"measure":[{"area":17.9,"points":[[380.0,492.5],[539.0,492.5],[539.0,610.5],[876.0,610.5],[876.0,933.5],[379.0,933.5]],"diagonals":[],"perimeter":18.8}],"area":17.9,"perimeter":18.8}""",
            contours = listOf(
                ExpectedContour(-1, 179, 188, 179, 188, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1120082,
            json = """{"room_id":1120082,"measure":[{"area":14.8,"points":[[304.0,598.5],[451.0,598.5],[451.0,666.5],[795.0,666.5],[795.0,946.5],[302.0,946.5]],"diagonals":[],"perimeter":16.8}],"area":14.8,"perimeter":16.8}""",
            contours = listOf(
                ExpectedContour(-1, 148, 168, 148, 168, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1120367,
            json = """{"room_id":1120367,"measure":[{"area":12.0,"points":[[271.2,40.7],[630.2,40.7],[630.2,388.7],[317.2,388.7],[317.2,278.7],[271.2,278.7]],"diagonals":[],"perimeter":14.1}],"area":12,"perimeter":14.1}""",
            contours = listOf(
                ExpectedContour(-1, 120, 141, 120, 141, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1120904,
            json = """{"room_id":1120904,"measure":[{"area":12.2,"points":[[470.0,579.5],[517.0,579.5],[517.0,564.5],[887.0,564.5],[887.0,884.5],[535.0,884.5],[535.0,715.5],[472.0,715.5]],"diagonals":[],"perimeter":14.7}],"area":12.2,"perimeter":14.7}""",
            contours = listOf(
                ExpectedContour(-1, 122, 147, 122, 147, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1120978,
            json = """{"room_id":1120978,"measure":[{"area":12.2,"points":[[444.0,511.5],[549.0,511.5],[549.0,591.5],[807.0,591.5],[807.0,448.5],[935.0,448.5],[935.0,712.5],[597.0,712.5],[597.0,948.5],[447.0,948.5]],"diagonals":[],"perimeter":21.4}],"area":12.2,"perimeter":21.4}""",
            contours = listOf(
                ExpectedContour(-1, 122, 214, 122, 214, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1121734,
            json = """{"room_id":1121734,"measure":[{"area":17.3,"points":[[391.0,504.5],[669.0,504.5],[669.0,1125.5],[391.0,1125.5]],"diagonals":[],"perimeter":18.0}],"area":17.3,"perimeter":18}""",
            contours = listOf(
                ExpectedContour(-1, 173, 180, 173, 180, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1121741,
            json = """{"room_id":1121741,"measure":[{"area":26.6,"points":[[311.0,377.5],[592.0,377.5],[592.0,1057.5],[191.0,1057.5],[191.0,492.5]],"diagonals":[],"perimeter":20.9}],"area":26.6,"perimeter":20.9}""",
            contours = listOf(
                ExpectedContour(-1, 266, 209, 266, 209, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1122224,
            json = """{"room_id":1122224,"measure":[{"area":17.4,"points":[[371.0,387.5],[718.0,387.5],[718.0,888.5],[371.0,888.5]],"diagonals":[],"perimeter":17.0}],"area":17.4,"perimeter":17}""",
            contours = listOf(
                ExpectedContour(-1, 174, 170, 174, 170, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1122281,
            json = """{"room_id":1122281,"measure":[{"area":13.5,"points":[[380.0,353.5],[528.0,353.5],[528.0,-290.5],[691.0,-290.5],[691.0,-77.5],[668.0,-77.5],[668.0,492.5],[381.0,492.5]],"diagonals":[],"perimeter":21.9}],"area":13.5,"perimeter":21.9}""",
            contours = listOf(
                ExpectedContour(-1, 135, 219, 135, 219, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1122365,
            json = """{"room_id":1122365,"measure":[{"area":10.5,"points":[[398.0,635.5],[698.0,635.5],[698.0,985.5],[398.0,985.5]],"diagonals":[],"perimeter":13.0}],"area":10.5,"perimeter":13}""",
            contours = listOf(
                ExpectedContour(-1, 105, 130, 105, 130, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1122400,
            json = """{"room_id":1122400,"measure":[{"area":18.7,"points":[[324.0,369.5],[700.0,369.5],[700.0,511.5],[728.0,511.5],[728.0,807.5],[701.0,807.5],[701.0,844.5],[324.0,844.5]],"diagonals":[],"perimeter":17.6}],"area":18.7,"perimeter":17.6}""",
            contours = listOf(
                ExpectedContour(-1, 187, 176, 187, 176, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1122621,
            json = """{"room_id":1122621,"measure":[{"area":9.7,"points":[[365.0,465.5],[670.0,465.5],[670.0,783.5],[365.0,783.5]],"diagonals":[],"perimeter":12.5}],"area":9.7,"perimeter":12.5}""",
            contours = listOf(
                ExpectedContour(-1, 97, 125, 97, 125, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1122685,
            json = """{"room_id":1122685,"measure":[{"area":10.6,"points":[[377.0,335.5],[431.0,335.5],[431.0,323.5],[693.0,323.5],[693.0,336.5],[698.0,336.5],[698.0,656.5],[378.0,656.5]],"diagonals":[],"perimeter":13.1}],"area":10.6,"perimeter":13.1}""",
            contours = listOf(
                ExpectedContour(-1, 106, 131, 106, 131, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1122860,
            json = """{"room_id":1122860,"measure":[{"area":20.6,"points":[[314.0,344.5],[729.0,344.5],[729.0,840.5],[314.0,840.5]],"diagonals":[],"perimeter":18.2}],"area":20.6,"perimeter":18.2}""",
            contours = listOf(
                ExpectedContour(-1, 206, 182, 206, 182, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1123028,
            json = """{"room_id":1123028,"measure":[{"area":1.7,"points":[[385.0,496.5],[545.0,496.5],[545.0,603.5],[385.0,603.5]],"diagonals":[],"perimeter":5.3}],"area":1.7,"perimeter":5.3}""",
            contours = listOf(
                ExpectedContour(-1, 17, 53, 17, 53, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1123036,
            json = """{"room_id":1123036,"measure":[{"area":13.8,"points":[[639.0,817.5],[1080.0,817.5],[1080.0,1130.5],[639.0,1130.5]],"diagonals":[],"perimeter":15.1}],"area":13.8,"perimeter":15.1}""",
            contours = listOf(
                ExpectedContour(-1, 138, 151, 138, 151, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1123049,
            json = """{"room_id":1123049,"measure":[{"area":10.4,"points":[[602.0,823.5],[931.0,823.5],[931.0,1140.5],[602.0,1140.5]],"diagonals":[],"perimeter":12.9}],"area":10.4,"perimeter":12.9}""",
            contours = listOf(
                ExpectedContour(-1, 104, 129, 104, 129, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1123093,
            json = """{"room_id":1123093,"measure":[{"area":18.0,"points":[[738.0,743.5],[1094.0,743.5],[1094.0,1250.5],[738.0,1250.5]],"diagonals":[],"perimeter":17.3}],"area":18,"perimeter":17.3}""",
            contours = listOf(
                ExpectedContour(-1, 180, 173, 180, 173, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1123336,
            json = """{"room_id":1123336,"measure":[{"area":16.0,"points":[[397.0,284.5],[890.0,284.5],[890.0,604.5],[792.0,604.5],[792.0,615.5],[512.0,615.5],[512.0,603.5],[400.0,603.5]],"diagonals":[],"perimeter":16.5}],"area":16,"perimeter":16.5}""",
            contours = listOf(
                ExpectedContour(-1, 160, 165, 160, 165, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1123348,
            json = """{"room_id":1123348,"measure":[{"area":16.1,"points":[[366.0,337.5],[766.0,337.5],[766.0,731.5],[666.0,731.5],[666.0,743.5],[388.0,743.5],[388.0,731.5],[366.0,731.5]],"diagonals":[],"perimeter":16.1}],"area":16.1,"perimeter":16.1}""",
            contours = listOf(
                ExpectedContour(-1, 161, 161, 161, 161, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1123502,
            json = """{"room_id":1123502,"measure":[{"area":11.5,"points":[[358.0,374.5],[755.0,374.5],[755.0,286.5],[988.0,286.5],[988.0,298.5],[1056.0,298.5],[1056.0,513.5],[948.0,513.5],[948.0,482.5],[490.0,482.5],[490.0,560.5],[360.0,560.5]],"diagonals":[],"perimeter":20.0}],"area":11.5,"perimeter":20}""",
            contours = listOf(
                ExpectedContour(-1, 115, 200, 115, 200, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1123610,
            json = """{"room_id":1123610,"measure":[{"area":17.6,"points":[[375.0,465.5],[485.0,465.5],[485.0,451.5],[740.0,451.5],[740.0,465.5],[862.0,465.5],[862.0,815.5],[742.0,815.5],[742.0,827.5],[487.0,827.5],[487.0,814.5],[378.0,814.5]],"diagonals":[],"perimeter":17.2}],"area":17.6,"perimeter":17.2}""",
            contours = listOf(
                ExpectedContour(-1, 176, 172, 176, 172, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1123753,
            json = """{"room_id":1123753,"measure":[{"area":14.9,"points":[[315.0,342.5],[746.0,342.5],[746.0,688.5],[315.0,688.5]],"diagonals":[],"perimeter":15.5}],"area":14.9,"perimeter":15.5}""",
            contours = listOf(
                ExpectedContour(-1, 149, 155, 149, 155, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1123967,
            json = """{"room_id":1123967,"measure":[{"area":19.2,"points":[[355.0,428.5],[810.0,428.5],[810.0,851.5],[355.0,851.5]],"diagonals":[],"perimeter":17.6}],"area":19.2,"perimeter":17.6}""",
            contours = listOf(
                ExpectedContour(-1, 192, 176, 192, 176, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1124187,
            json = """{"room_id":1124187,"measure":[{"area":10.1,"points":[[183.7,113.0],[456.7,113.0],[456.7,477.0],[326.7,477.0],[326.7,491.0],[184.7,491.0]],"diagonals":[],"perimeter":13.0}],"area":10.1,"perimeter":13}""",
            contours = listOf(
                ExpectedContour(-1, 101, 130, 101, 130, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1124544,
            json = """{"room_id":1124544,"measure":[{"area":14.5,"points":[[400.0,407.5],[963.0,407.5],[963.0,447.5],[1003.0,447.5],[1003.0,668.5],[588.0,668.5],[588.0,607.5],[398.0,607.5]],"diagonals":[],"perimeter":17.3}],"area":14.5,"perimeter":17.3}""",
            contours = listOf(
                ExpectedContour(-1, 145, 173, 145, 173, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1124601,
            json = """{"room_id":1124601,"measure":[{"area":19.6,"points":[[330.0,357.5],[488.0,357.5],[488.0,430.5],[902.0,430.5],[902.0,752.5],[329.0,752.5]],"diagonals":[],"perimeter":19.4}],"area":19.6,"perimeter":19.4}""",
            contours = listOf(
                ExpectedContour(-1, 196, 194, 196, 194, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1124659,
            json = """{"room_id":1124659,"measure":[{"area":16.1,"points":[[356.0,333.5],[769.0,333.5],[769.0,713.5],[647.0,713.5],[647.0,726.5],[355.0,726.5]],"diagonals":[],"perimeter":16.1}],"area":16.1,"perimeter":16.1}""",
            contours = listOf(
                ExpectedContour(-1, 161, 161, 161, 161, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1125352,
            json = """{"room_id":1125352,"measure":[{"area":12.3,"points":[[390.0,518.5],[790.0,518.5],[790.0,829.5],[399.0,829.5],[399.0,622.5],[389.0,622.5]],"diagonals":[],"perimeter":14.2}],"area":12.3,"perimeter":14.2}""",
            contours = listOf(
                ExpectedContour(-1, 123, 142, 123, 142, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1125512,
            json = """{"room_id":1125512,"measure":[{"area":19.1,"points":[[364.0,374.5],[774.0,374.5],[774.0,839.5],[364.0,839.5]],"diagonals":[],"perimeter":17.5}],"area":19.1,"perimeter":17.5}""",
            contours = listOf(
                ExpectedContour(-1, 191, 175, 191, 175, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1125725,
            json = """{"room_id":1125725,"measure":[{"area":10.1,"points":[[431.0,455.5],[768.0,455.5],[768.0,755.5],[431.0,755.5]],"diagonals":[],"perimeter":12.7}],"area":10.1,"perimeter":12.7}""",
            contours = listOf(
                ExpectedContour(-1, 101, 127, 101, 127, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1126687,
            json = """{"room_id":1126687,"measure":[{"area":9.9,"points":[[371.0,498.5],[689.0,498.5],[689.0,808.5],[371.0,808.5]],"diagonals":[],"perimeter":12.6}],"area":9.9,"perimeter":12.6}""",
            contours = listOf(
                ExpectedContour(-1, 99, 126, 99, 126, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1127152,
            json = """{"room_id":1127152,"measure":[{"area":26.4,"points":[[338.0,313.5],[618.0,313.5],[618.0,990.5],[218.0,990.5],[218.0,429.5]],"diagonals":[],"perimeter":20.8}],"area":26.4,"perimeter":20.8}""",
            contours = listOf(
                ExpectedContour(-1, 264, 208, 264, 208, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1127460,
            json = """{"room_id":1127460,"measure":[{"area":11.4,"points":[[390.0,588.0],[495.0,588.0],[495.0,577.0],[640.0,577.0],[640.0,588.0],[749.0,588.0],[749.0,898.0],[640.0,898.0],[640.0,909.0],[495.0,909.0],[495.0,898.0],[390.0,898.0]],"diagonals":[],"perimeter":13.8}],"area":11.4,"perimeter":13.8}""",
            contours = listOf(
                ExpectedContour(-1, 114, 138, 114, 138, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1127706,
            json = """{"room_id":1127706,"measure":[{"area":14.6,"points":[[197.0,256.0],[372.0,257.0],[372.0,287.0],[606.0,287.0],[606.0,172.0],[655.0,172.0],[654.0,81.0],[762.0,81.0],[762.0,277.0],[794.0,277.0],[794.0,394.0],[524.0,394.0],[524.0,477.0],[501.0,477.0],[502.0,548.0],[196.0,548.0]],"diagonals":[],"perimeter":21.9}],"area":14.6,"perimeter":21.9}""",
            contours = listOf(
                ExpectedContour(-1, 146, 219, 146, 219, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1127969,
            json = """{"room_id":1127969,"measure":[{"area":18.0,"points":[[414.0,418.0],[745.0,418.0],[745.0,963.0],[414.0,963.0]],"diagonals":[],"perimeter":17.5}],"area":18,"perimeter":17.5}""",
            contours = listOf(
                ExpectedContour(-1, 180, 175, 180, 175, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1128011,
            json = """{"room_id":1128011,"measure":[{"area":18.6,"points":[[378.0,315.0],[860.0,315.0],[860.0,700.0],[378.0,700.0]],"diagonals":[],"perimeter":17.3}],"area":18.6,"perimeter":17.3}""",
            contours = listOf(
                ExpectedContour(-1, 186, 173, 186, 173, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1128398,
            json = """{"room_id":1128398,"measure":[{"area":4.3,"points":[[366.0,418.0],[446.0,418.0],[446.0,446.0],[556.0,446.0],[556.0,663.0],[366.0,663.0]],"diagonals":[],"perimeter":8.7}],"area":4.3,"perimeter":8.7}""",
            contours = listOf(
                ExpectedContour(-1, 43, 87, 43, 87, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1128506,
            json = """{"room_id":1128506,"measure":[{"area":13.2,"points":[[342.0,494.5],[432.0,494.5],[432.0,199.5],[708.0,199.5],[708.0,240.5],[749.0,240.5],[749.0,592.5],[343.0,592.5]],"diagonals":[],"perimeter":16.0}],"area":13.2,"perimeter":16}""",
            contours = listOf(
                ExpectedContour(-1, 132, 160, 132, 160, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1128560,
            json = """{"room_id":1128560,"measure":[{"area":10.4,"points":[[338.0,497.5],[416.0,497.5],[416.0,377.5],[709.0,377.5],[709.0,682.5],[338.0,682.5]],"diagonals":[],"perimeter":13.5}],"area":10.4,"perimeter":13.5}""",
            contours = listOf(
                ExpectedContour(-1, 104, 135, 104, 135, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1128564,
            json = """{"room_id":1128564,"measure":[{"area":10.4,"points":[[338.0,497.5],[413.0,497.5],[413.0,377.5],[709.0,377.5],[709.0,682.5],[338.0,682.5]],"diagonals":[],"perimeter":13.5}],"area":10.4,"perimeter":13.5}""",
            contours = listOf(
                ExpectedContour(-1, 104, 135, 104, 135, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1128752,
            json = """{"room_id":1128752,"measure":[{"area":13.5,"points":[[386.0,484.5],[874.0,484.5],[874.0,761.5],[386.0,761.5]],"diagonals":[],"perimeter":15.3}],"area":13.5,"perimeter":15.3}""",
            contours = listOf(
                ExpectedContour(-1, 135, 153, 135, 153, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1128786,
            json = """{"room_id":1128786,"measure":[{"area":15.3,"points":[[418.0,298.5],[835.0,298.5],[835.0,664.5],[418.0,664.5]],"diagonals":[],"perimeter":15.7}],"area":15.3,"perimeter":15.7}""",
            contours = listOf(
                ExpectedContour(-1, 153, 157, 153, 157, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1128852,
            json = """{"room_id":1128852,"measure":[{"area":4.9,"points":[[376.0,420.5],[516.0,420.5],[516.0,486.5],[552.0,486.5],[552.0,710.5],[376.0,710.5]],"diagonals":[],"perimeter":9.3}],"area":4.9,"perimeter":9.3}""",
            contours = listOf(
                ExpectedContour(-1, 49, 93, 49, 93, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1129144,
            json = """{"room_id":1129144,"measure":[{"area":15.8,"points":[[438.0,399.5],[836.0,399.5],[836.0,796.5],[438.0,796.5]],"diagonals":[],"perimeter":15.9}],"area":15.8,"perimeter":15.9}""",
            contours = listOf(
                ExpectedContour(-1, 158, 159, 158, 159, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1129187,
            json = """{"room_id":1129187,"measure":[{"area":11.4,"points":[[358.0,531.5],[779.0,531.5],[779.0,801.5],[358.0,801.5]],"diagonals":[],"perimeter":13.8}],"area":11.4,"perimeter":13.8}""",
            contours = listOf(
                ExpectedContour(-1, 114, 138, 114, 138, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1129392,
            json = """{"room_id":1129392,"measure":[{"area":2.7,"points":[[368.0,460.5],[427.0,460.5],[427.0,513.5],[539.0,513.5],[539.0,569.5],[489.0,569.5],[489.0,684.5],[366.0,684.5]],"diagonals":[],"perimeter":7.9}],"area":2.7,"perimeter":7.9}""",
            contours = listOf(
                ExpectedContour(-1, 27, 79, 27, 79, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1129524,
            json = """{"room_id":1129524,"measure":[{"area":4.8,"points":[[438.0,473.5],[535.0,473.5],[535.0,521.5],[662.0,521.5],[662.0,626.5],[642.0,626.5],[642.0,714.5],[554.0,714.5],[554.0,726.5],[437.0,726.5]],"diagonals":[],"perimeter":9.6}],"area":4.8,"perimeter":9.6}""",
            contours = listOf(
                ExpectedContour(-1, 48, 96, 48, 96, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1129699,
            json = """{"room_id":1129699,"measure":[{"area":1.4,"points":[[370.0,419.5],[515.0,419.5],[515.0,517.5],[370.0,517.5]],"diagonals":[],"perimeter":4.9}],"area":1.4,"perimeter":4.9}""",
            contours = listOf(
                ExpectedContour(-1, 14, 49, 14, 49, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1129840,
            json = """{"room_id":1129840,"measure":[{"area":4.1,"points":[[517.0,526.5],[692.0,526.5],[692.0,763.5],[517.0,763.5]],"diagonals":[],"perimeter":8.2}],"area":4.1,"perimeter":8.2}""",
            contours = listOf(
                ExpectedContour(-1, 41, 82, 41, 82, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1129976,
            json = """{"room_id":1129976,"measure":[{"area":19.8,"points":[[321.0,395.5],[391.0,395.5],[391.0,-19.5],[716.0,-19.5],[716.0,554.5],[320.0,554.5]],"diagonals":[],"perimeter":19.4}],"area":19.8,"perimeter":19.4}""",
            contours = listOf(
                ExpectedContour(-1, 198, 194, 198, 194, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1130140,
            json = """{"room_id":1130140,"measure":[{"area":17.7,"points":[[589.0,434.5],[1006.0,434.5],[1006.0,859.5],[589.0,859.5]],"diagonals":[],"perimeter":16.8}],"area":17.7,"perimeter":16.8}""",
            contours = listOf(
                ExpectedContour(-1, 177, 168, 177, 168, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1130355,
            json = """{"room_id":1130355,"measure":[{"area":5.0,"points":[[373.0,451.5],[471.0,451.5],[471.0,957.5],[373.0,957.5]],"diagonals":[],"perimeter":12.1}],"area":5,"perimeter":12.1}""",
            contours = listOf(
                ExpectedContour(-1, 50, 121, 50, 121, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1131133,
            json = """{"room_id":1131133,"measure":[{"area":17.4,"points":[[355.0,654.5],[635.0,654.5],[635.0,1274.5],[355.0,1274.5]],"diagonals":[],"perimeter":18.0}],"area":17.4,"perimeter":18}""",
            contours = listOf(
                ExpectedContour(-1, 174, 180, 174, 180, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1131560,
            json = """{"room_id":1131560,"measure":[{"area":11.3,"points":[[356.0,359.5],[699.0,359.5],[699.0,689.5],[356.0,689.5]],"diagonals":[],"perimeter":13.5}],"area":11.3,"perimeter":13.5}""",
            contours = listOf(
                ExpectedContour(-1, 113, 135, 113, 135, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1131823,
            json = """{"room_id":1131823,"measure":[{"area":18.5,"points":[[380.0,451.5],[513.0,451.5],[513.0,425.5],[838.0,425.5],[838.0,835.5],[379.0,835.5]],"diagonals":[],"perimeter":17.4}],"area":18.5,"perimeter":17.4}""",
            contours = listOf(
                ExpectedContour(-1, 185, 174, 185, 174, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1131864,
            json = """{"room_id":1131864,"measure":[{"area":9.6,"points":[[369.0,290.5],[419.0,290.5],[419.0,-160.5],[594.0,-160.5],[594.0,-39.5],[531.0,-39.5],[531.0,382.5],[554.0,382.5],[554.0,507.5],[370.0,507.5]],"diagonals":[],"perimeter":18.3}],"area":9.6,"perimeter":18.3}""",
            contours = listOf(
                ExpectedContour(-1, 96, 183, 96, 183, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1132313,
            json = """{"room_id":1132313,"measure":[{"area":5.0,"points":[[385.0,500.5],[704.0,500.5],[704.0,627.5],[525.0,627.5],[525.0,697.5],[385.0,697.5]],"diagonals":[],"perimeter":10.3}],"area":5,"perimeter":10.3}""",
            contours = listOf(
                ExpectedContour(-1, 50, 103, 50, 103, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1132395,
            json = """{"room_id":1132395,"measure":[{"area":8.2,"points":[[419.0,393.5],[578.0,393.5],[578.0,408.5],[655.0,408.5],[655.0,526.5],[802.0,526.5],[802.0,541.5],[890.0,541.5],[890.0,676.5],[642.0,676.5],[642.0,712.5],[543.0,712.5],[543.0,506.5],[415.0,506.5]],"diagonals":[],"perimeter":15.8}],"area":8.2,"perimeter":15.8}""",
            contours = listOf(
                ExpectedContour(-1, 82, 158, 82, 158, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1132607,
            json = """{"room_id":1132607,"measure":[{"area":10.2,"points":[[357.0,448.5],[574.0,448.5],[574.0,564.5],[506.0,564.5],[506.0,984.5],[638.0,984.5],[638.0,1092.5],[396.0,1092.5],[396.0,663.5],[355.0,663.5]],"diagonals":[],"perimeter":19.9}],"area":10.2,"perimeter":19.9}""",
            contours = listOf(
                ExpectedContour(-1, 102, 199, 102, 199, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1132957,
            json = """{"room_id":1132957,"measure":[{"area":19.4,"points":[[418.0,478.5],[800.0,478.5],[800.0,985.5],[418.0,985.5]],"diagonals":[],"perimeter":17.8}],"area":19.4,"perimeter":17.8}""",
            contours = listOf(
                ExpectedContour(-1, 194, 178, 194, 178, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1132967,
            json = """{"room_id":1132967,"measure":[{"area":15.1,"points":[[346.0,295.5],[674.0,295.5],[674.0,754.5],[346.0,754.5]],"diagonals":[],"perimeter":15.7}],"area":15.1,"perimeter":15.7}""",
            contours = listOf(
                ExpectedContour(-1, 151, 157, 151, 157, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.RANDOM,
            roomId = 1133974,
            json = """{"room_id":1133974,"measure":[{"area":16.8,"points":[[410.0,411.5],[751.0,411.5],[751.0,903.5],[410.0,903.5]],"diagonals":[],"perimeter":16.7}],"area":16.8,"perimeter":16.7}""",
            contours = listOf(
                ExpectedContour(-1, 168, 167, 168, 167, true),
            ),
            note = "Текущие данные: площадь и периметр из точек совпадают с сохранёнными.",
        ),
        MeasureFixture(
            tag = FixtureTag.BOUNDARY,
            roomId = 1116126,
            json = """{"room_id":1116126,"measure":[{"area":10.3,"points":[[315.0,346.0],[435.0,346.0],[435.0,356.0],[625.0,356.0],[625.0,686.0],[315.0,686.0]],"diagonals":[],"perimeter":13.0}],"area":10.3,"perimeter":13}""",
            contours = listOf(
                ExpectedContour(-1, 104, 130, 103, 130, true),
            ),
            note = "Площадь ровно x.x5 м²: старый web округлил вниз, новое правило half-up — вверх.",
        ),
        MeasureFixture(
            tag = FixtureTag.BOUNDARY,
            roomId = 1122453,
            json = """{"room_id":1122453,"measure":[{"area":16.1,"points":[[354.0,396.5],[779.0,396.5],[779.0,776.5],[354.0,776.5]],"diagonals":[],"perimeter":16.1}],"area":16.1,"perimeter":16.1}""",
            contours = listOf(
                ExpectedContour(-1, 162, 161, 161, 161, true),
            ),
            note = "Площадь ровно x.x5 м²: старый web округлил вниз, новое правило half-up — вверх.",
        ),
        MeasureFixture(
            tag = FixtureTag.BOUNDARY,
            roomId = 1124560,
            json = """{"room_id":1124560,"measure":[{"area":5.9,"points":[[399.0,376.5],[588.0,376.5],[588.0,191.5],[688.0,191.5],[689.0,516.5],[564.0,516.0],[564.0,520.0],[400.0,519.0]],"diagonals":[],"perimeter":12.3}],"area":5.9,"perimeter":12.3}""",
            contours = listOf(
                ExpectedContour(-1, 60, 123, 59, 123, true),
            ),
            note = "Площадь 5,95145 по точкам; сохранено 5,9 — устаревшее значение (см. #4).",
        ),
        MeasureFixture(
            tag = FixtureTag.BOUNDARY,
            roomId = 1126502,
            json = """{"room_id":1126502,"measure":[{"area":13.6,"points":[[250.4,306.8],[600.4,306.8],[600.4,696.8],[250.4,696.8]],"diagonals":[],"perimeter":14.8}],"area":13.6,"perimeter":14.8}""",
            contours = listOf(
                ExpectedContour(-1, 137, 148, 136, 148, true),
            ),
            note = "Площадь ровно x.x5 м²: старый web округлил вниз, новое правило half-up — вверх.",
        ),
        MeasureFixture(
            tag = FixtureTag.BOUNDARY,
            roomId = 1128725,
            json = """{"room_id":1128725,"measure":[{"area":1.1,"points":[[394.0,658.5],[494.0,658.5],[494.0,773.5],[394.0,773.5]],"diagonals":[],"perimeter":4.3}],"area":1.1,"perimeter":4.3}""",
            contours = listOf(
                ExpectedContour(-1, 12, 43, 11, 43, true),
            ),
            note = "Площадь ровно x.x5 м²: старый web округлил вниз, новое правило half-up — вверх.",
        ),
        MeasureFixture(
            tag = FixtureTag.BOUNDARY,
            roomId = 1128869,
            json = """{"room_id":1128869,"measure":[{"area":4.3,"points":[[311.0,506.5],[485.0,506.5],[485.0,756.5],[311.0,756.5]],"diagonals":[],"perimeter":8.5}],"area":4.3,"perimeter":8.5}""",
            contours = listOf(
                ExpectedContour(-1, 44, 85, 43, 85, true),
            ),
            note = "Площадь ровно x.x5 м²: старый web округлил вниз, новое правило half-up — вверх.",
        ),
        MeasureFixture(
            tag = FixtureTag.BOUNDARY,
            roomId = 1131868,
            json = """{"room_id":1131868,"measure":[{"area":16.4,"points":[[779.0,540.0],[1249.0,540.0],[1249.0,890.0],[779.0,890.0]],"diagonals":[],"perimeter":16.4}],"area":16.4,"perimeter":16.4}""",
            contours = listOf(
                ExpectedContour(-1, 165, 164, 164, 164, true),
            ),
            note = "Площадь ровно x.x5 м²: старый web округлил вниз, новое правило half-up — вверх.",
        ),
        MeasureFixture(
            tag = FixtureTag.BOUNDARY,
            roomId = 1132399,
            json = """{"room_id":1132399,"measure":[{"area":18.4,"points":[[400.0,431.5],[858.0,431.5],[858.0,839.5],[531.0,839.5],[531.0,818.5],[398.0,818.5]],"diagonals":[],"perimeter":17.3}],"area":18.4,"perimeter":17.3}""",
            contours = listOf(
                ExpectedContour(-1, 185, 173, 184, 173, true),
            ),
            note = "Площадь ровно x.x5 м²: старый web округлил вниз, новое правило half-up — вверх.",
        ),
        MeasureFixture(
            tag = FixtureTag.REVERSE,
            roomId = 414,
            json = """{"room_id":414,"measure":[{"area":2.5,"points":[[275.0,163.0],[275.0,337.0],[433.0,337.0],[433.0,188.0],[353.0,188.0],[353.0,163.0]],"diagonals":[],"perimeter":6.6}],"area":2.5,"perimeter":6.6}""",
            contours = listOf(
                ExpectedContour(1, 25, 66, 25, 66, true),
            ),
            note = "Обратный обход (отрицательная знаковая площадь): после нормализации площадь та же.",
        ),
        MeasureFixture(
            tag = FixtureTag.REVERSE,
            roomId = 442,
            json = """{"room_id":442,"measure":[{"area":2.1,"points":[[566.0,102.0],[393.0,102.0],[393.0,221.0],[566.0,221.0]],"diagonals":[],"perimeter":5.8}],"area":2.1,"perimeter":5.8}""",
            contours = listOf(
                ExpectedContour(1, 21, 58, 21, 58, true),
            ),
            note = "Обратный обход (отрицательная знаковая площадь): после нормализации площадь та же.",
        ),
        MeasureFixture(
            tag = FixtureTag.REVERSE,
            roomId = 514,
            json = """{"room_id":514,"measure":[{"area":2.7,"points":[[455.0,164.0],[338.0,164.0],[338.0,393.0],[455.0,393.0]],"diagonals":[],"perimeter":6.9}],"area":2.7,"perimeter":6.9}""",
            contours = listOf(
                ExpectedContour(1, 27, 69, 27, 69, true),
            ),
            note = "Обратный обход (отрицательная знаковая площадь): после нормализации площадь та же.",
        ),
        MeasureFixture(
            tag = FixtureTag.REVERSE,
            roomId = 143181,
            json = """{"room_id":143181,"measure":[{"area":17.0,"points":[[58.0,59.0],[59.0,376.0],[414.0,376.0],[414.0,467.0],[554.0,466.0],[554.0,58.0]],"diagonals":[],"perimeter":18.1}],"area":17,"perimeter":18.1}""",
            contours = listOf(
                ExpectedContour(1, 170, 181, 170, 181, true),
            ),
            note = "Обратный обход (отрицательная знаковая площадь): после нормализации площадь та же.",
        ),
        MeasureFixture(
            tag = FixtureTag.REVERSE,
            roomId = 143370,
            json = """{"room_id":143370,"measure":[{"area":10.6,"points":[[460.0,350.0],[459.0,251.0],[408.0,251.0],[408.0,60.0],[61.0,60.0],[60.0,350.0]],"diagonals":[],"perimeter":13.8}],"area":10.6,"perimeter":13.8}""",
            contours = listOf(
                ExpectedContour(1, 106, 138, 106, 138, true),
            ),
            note = "Обратный обход (отрицательная знаковая площадь): после нормализации площадь та же.",
        ),
        MeasureFixture(
            tag = FixtureTag.REVERSE,
            roomId = 144063,
            json = """{"room_id":144063,"measure":[{"area":10.4,"points":[[115.0,60.0],[114.0,370.0],[486.0,370.0],[486.0,249.0],[427.0,249.0],[427.0,60.0]],"diagonals":[],"perimeter":13.6}],"area":10.4,"perimeter":13.6}""",
            contours = listOf(
                ExpectedContour(1, 104, 136, 104, 136, true),
            ),
            note = "Обратный обход (отрицательная знаковая площадь): после нормализации площадь та же.",
        ),
        MeasureFixture(
            tag = FixtureTag.REVERSE,
            roomId = 585337,
            json = """{"room_id":585337,"measure":[{"area":11.1,"points":[[247.0,104.0],[130.0,104.0],[130.0,227.0],[61.0,227.0],[61.0,325.0],[124.0,325.0],[124.0,353.0],[247.0,353.0]],"diagonals":[],"perimeter":18.0}],"area":3.6,"perimeter":8.7}""",
            contours = listOf(
                ExpectedContour(1, 36, 87, 111, 180, true),
            ),
            note = "Legacy: обратный обход + устаревшие area/perimeter (11,1/18,0 при 3,6/8,7 по точкам).",
        ),
        MeasureFixture(
            tag = FixtureTag.DEGENERATE,
            roomId = 191994,
            json = """{"room_id":191994,"measure":[{"area":2.2,"points":[[60.0,131.0],[60.0,132.0]],"diagonals":[],"perimeter":7.5}],"area":0,"perimeter":0}""",
            contours = listOf(
                ExpectedContour(0, 0, 0, 22, 75, false),
            ),
            note = "Legacy: 2 точки, но сохранены area/perimeter ненулевые — геометрия потеряна.",
        ),
        MeasureFixture(
            tag = FixtureTag.DEGENERATE,
            roomId = 237594,
            json = """{"room_id":237594,"measure":[{"area":1.9,"points":[[59.0,479.0],[59.0,480.0]],"diagonals":[],"perimeter":6.5}],"area":1.9,"perimeter":6.5}""",
            contours = listOf(
                ExpectedContour(0, 0, 0, 19, 65, false),
            ),
            note = "Legacy: 2 точки, но сохранены area/perimeter ненулевые — геометрия потеряна.",
        ),
        MeasureFixture(
            tag = FixtureTag.DEGENERATE,
            roomId = 808705,
            json = """{"room_id":808705,"measure":[{"area":14.4,"points":[[59.0,449.0],[60.0,449.0]],"diagonals":[],"perimeter":15.4}],"area":14.4,"perimeter":15.4}""",
            contours = listOf(
                ExpectedContour(0, 0, 0, 144, 154, false),
            ),
            note = "Legacy: 2 точки, но сохранены area/perimeter ненулевые — геометрия потеряна.",
        ),
        MeasureFixture(
            tag = FixtureTag.DEGENERATE,
            roomId = 1050856,
            json = """{"room_id":1050856,"measure":[{"area":0.0,"points":[[446.0,767.0],[446.0,767.0],[446.0,767.0],[446.0,767.0]],"diagonals":[],"perimeter":0.0}],"area":0,"perimeter":0}""",
            contours = listOf(
                ExpectedContour(0, 0, 0, 0, 0, false),
            ),
            note = "Все точки совпадают.",
        ),
        MeasureFixture(
            tag = FixtureTag.DEGENERATE,
            roomId = 1087391,
            json = """{"room_id":1087391,"measure":[{"area":0.0,"points":[[250.0,818.0],[249.4,818.0]],"diagonals":[],"perimeter":0.0}],"area":0,"perimeter":0}""",
            contours = listOf(
                ExpectedContour(0, 0, 0, 0, 0, false),
            ),
            note = "2 точки на расстоянии 0,6 см.",
        ),
        MeasureFixture(
            tag = FixtureTag.DEGENERATE,
            roomId = 1087583,
            json = """{"room_id":1087583,"measure":[{"area":0.0,"points":[[162.0,635.0],[162.0,636.0],[163.0,636.0],[162.0,636.0]],"diagonals":[],"perimeter":0.0}],"area":0,"perimeter":0}""",
            contours = listOf(
                ExpectedContour(0, 0, 0, 0, 0, false),
            ),
            note = "Нулевая площадь: открывается, сохранение запрещено.",
        ),
        MeasureFixture(
            tag = FixtureTag.EMPTY,
            roomId = 1033389,
            json = """{"room_id":1033389,"measure":[{"area":6.8,"points":[[400.0,313.0],[504.0,313.0],[504.0,372.0],[667.0,372.0],[667.0,407.0],[734.0,407.0],[734.0,580.0],[482.0,580.0],[482.0,511.0],[400.0,511.0]],"diagonals":[],"perimeter":12.0},{"area":0.0,"points":[],"diagonals":[],"perimeter":0.0}],"area":6.8,"perimeter":12}""",
            contours = listOf(
                ExpectedContour(-1, 68, 120, 68, 120, true),
                ExpectedContour(0, 0, 0, 0, 0, false),
            ),
            note = "Есть контур с пустым points.",
        ),
        MeasureFixture(
            tag = FixtureTag.EMPTY,
            roomId = 1047117,
            json = """{"room_id":1047117,"measure":[{"area":0.0,"points":[],"diagonals":[],"perimeter":0.0}],"area":0,"perimeter":0}""",
            contours = listOf(
                ExpectedContour(0, 0, 0, 0, 0, false),
            ),
            note = "Есть контур с пустым points.",
        ),
        MeasureFixture(
            tag = FixtureTag.EMPTY,
            roomId = 1094055,
            json = """{"room_id":1094055,"measure":[{"area":0.0,"points":[],"diagonals":[],"perimeter":0.0},{"area":3.8,"points":[[350.1,574.0],[640.1,574.0],[640.1,704.0],[350.1,704.0]],"diagonals":[],"perimeter":8.4}],"area":3.8,"perimeter":8.4}""",
            contours = listOf(
                ExpectedContour(0, 0, 0, 0, 0, false),
                ExpectedContour(-1, 38, 84, 38, 84, true),
            ),
            note = "Есть контур с пустым points.",
        ),
    )

    fun byTag(tag: FixtureTag): List<MeasureFixture> = all.filter { it.tag == tag }
}
