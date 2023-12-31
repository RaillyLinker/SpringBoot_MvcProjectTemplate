package com.railly_linker.springboot_mvc_project_template.controllers.c7_service1_tk_v1_mapCoordinateCalculation

import com.railly_linker.springboot_mvc_project_template.annotations.CustomTransactional
import com.railly_linker.springboot_mvc_project_template.configurations.database_configs.Database1Config
import com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.repositories.Database1_NativeRepository
import com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.repositories.Database1_Template_TestMapRepository
import com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.tables.Database1_Template_TestMap
import com.railly_linker.springboot_mvc_project_template.custom_objects.MapCoordinateUtilObject
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class C7Service1TkV1MapCoordinateCalculationService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    // (Database1 Repository)
    private val database1TemplateTestMapRepository: Database1_Template_TestMapRepository,
    private val database1NativeRepository: Database1_NativeRepository
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api0(httpServletResponse: HttpServletResponse) {
        database1TemplateTestMapRepository.deleteAll()

        val latLngList: List<Pair<Double, Double>> = listOf(
            Pair(37.5845885, 127.0001891),
            Pair(37.6060504, 126.9607987),
            Pair(37.5844214, 126.9699813),
            Pair(37.5757558, 126.9710255),
            Pair(37.5764907, 126.968655),
            Pair(37.5786667, 127.0156223),
            Pair(37.561697, 126.9968491),
            Pair(37.5880051, 127.0181872),
            Pair(37.5713246, 126.9635654),
            Pair(37.5922066, 127.0135319),
            Pair(37.5690038, 126.9632755),
            Pair(37.584865, 126.948639),
            Pair(37.5690454, 127.0232121),
            Pair(37.5634635, 127.015948),
            Pair(37.5748642, 127.0155003),
            Pair(37.5708604, 126.9612919),
            Pair(37.5570078, 126.9533333),
            Pair(37.5726188, 127.0576283),
            Pair(37.5914225, 127.0129648),
            Pair(37.5659102, 127.0217363)
        )

        for (latLng in latLngList) {
            database1TemplateTestMapRepository.save(
                Database1_Template_TestMap(
                    latLng.first,
                    latLng.second,
                    true
                )
            )
        }
        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api1(
        httpServletResponse: HttpServletResponse,
        latitude1: Double,
        longitude1: Double,
        latitude2: Double,
        longitude2: Double
    ): C7Service1TkV1MapCoordinateCalculationController.Api1OutputVo? {
        httpServletResponse.setHeader("api-result-code", "0")
        return C7Service1TkV1MapCoordinateCalculationController.Api1OutputVo(
            MapCoordinateUtilObject.getDistanceMeterBetweenTwoLatLngCoordinate(
                Pair(latitude1, longitude1),
                Pair(latitude2, longitude2)
            )
        )
    }


    ////
    fun api2(
        httpServletResponse: HttpServletResponse,
        inputVo: C7Service1TkV1MapCoordinateCalculationController.Api2InputVo
    ): C7Service1TkV1MapCoordinateCalculationController.Api2OutputVo? {
        val latLngCoordinate = ArrayList<Pair<Double, Double>>()

        for (coordinate in inputVo.coordinateList) {
            latLngCoordinate.add(
                Pair(coordinate.centerLatitude, coordinate.centerLongitude)
            )
        }

        val centerCoordinate = MapCoordinateUtilObject.getCenterLatLngCoordinate(
            latLngCoordinate
        )

        httpServletResponse.setHeader("api-result-code", "0")
        return C7Service1TkV1MapCoordinateCalculationController.Api2OutputVo(
            centerCoordinate.first,
            centerCoordinate.second
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api3(
        httpServletResponse: HttpServletResponse,
        inputVo: C7Service1TkV1MapCoordinateCalculationController.Api3InputVo
    ): C7Service1TkV1MapCoordinateCalculationController.Api3OutputVo? {
        database1TemplateTestMapRepository.save(
            Database1_Template_TestMap(
                inputVo.latitude,
                inputVo.longitude,
                true
            )
        )

        val coordinateList = ArrayList<C7Service1TkV1MapCoordinateCalculationController.Api3OutputVo.Coordinate>()
        val latLngCoordinate = ArrayList<Pair<Double, Double>>()

        for (testMap in database1TemplateTestMapRepository.findAll()) {
            coordinateList.add(
                C7Service1TkV1MapCoordinateCalculationController.Api3OutputVo.Coordinate(
                    testMap.latitude,
                    testMap.longitude
                )
            )

            latLngCoordinate.add(
                Pair(testMap.latitude, testMap.longitude)
            )
        }

        val centerCoordinate = MapCoordinateUtilObject.getCenterLatLngCoordinate(
            latLngCoordinate
        )

        httpServletResponse.setHeader("api-result-code", "0")
        return C7Service1TkV1MapCoordinateCalculationController.Api3OutputVo(
            coordinateList,
            C7Service1TkV1MapCoordinateCalculationController.Api3OutputVo.Coordinate(
                centerCoordinate.first,
                centerCoordinate.second
            )
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api4(httpServletResponse: HttpServletResponse) {
        database1TemplateTestMapRepository.deleteAll()
        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api5(
        httpServletResponse: HttpServletResponse,
        anchorLatitude: Double,
        anchorLongitude: Double,
        radiusKiloMeter: Double
    ): C7Service1TkV1MapCoordinateCalculationController.Api5OutputVo? {
        val entityList =
            database1NativeRepository.selectListForApiC7N5(
                anchorLatitude,
                anchorLongitude,
                radiusKiloMeter
            )

        val coordinateCalcResultList = ArrayList<C7Service1TkV1MapCoordinateCalculationController.Api5OutputVo.CoordinateCalcResult>()
        for (entity in entityList) {
            coordinateCalcResultList.add(
                C7Service1TkV1MapCoordinateCalculationController.Api5OutputVo.CoordinateCalcResult(
                    entity.uid,
                    entity.latitude,
                    entity.longitude,
                    entity.distanceKiloMeter
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C7Service1TkV1MapCoordinateCalculationController.Api5OutputVo(
            coordinateCalcResultList
        )
    }


    ////
    fun api6(
        httpServletResponse: HttpServletResponse,
        northLatitude: Double, // 북위도 (ex : 37.771848)
        eastLongitude: Double, // 동경도 (ex : 127.433549)
        southLatitude: Double, // 남위도 (ex : 37.245683)
        westLongitude: Double // 남경도 (ex : 126.587602)
    ): C7Service1TkV1MapCoordinateCalculationController.Api6OutputVo? {
        val entityList =
            database1NativeRepository.selectListForApiC7N6(
                northLatitude,
                eastLongitude,
                southLatitude,
                westLongitude
            )

        val coordinateCalcResultList = ArrayList<C7Service1TkV1MapCoordinateCalculationController.Api6OutputVo.CoordinateCalcResult>()
        for (entity in entityList) {
            coordinateCalcResultList.add(
                C7Service1TkV1MapCoordinateCalculationController.Api6OutputVo.CoordinateCalcResult(
                    entity.uid,
                    entity.latitude,
                    entity.longitude
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C7Service1TkV1MapCoordinateCalculationController.Api6OutputVo(
            coordinateCalcResultList
        )
    }


    ////
}