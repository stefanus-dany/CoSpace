package id.stefanusdany.cospace.ui.user.recommendation

import java.util.Collections
import kotlin.math.pow
import kotlin.math.sqrt
import id.stefanusdany.cospace.data.entity.ResultRecommendationEntity
import id.stefanusdany.cospace.data.entity.TopsisEntity

class Topsis {

    var bobotHarga: Int = 0
    var bobotJarak = 0
    var bobotRating = 0

    lateinit var dataCoWorking: List<TopsisEntity>

    var jmlNilaiAlternatifJarak = 0.0
    var jmlNilaiAlternatifHarga = 0.0
    var jmlNilaiAlternatifRating = 0.0

    var hasilNormalisasiAlternatifJarak = mutableListOf<Double>()
    var hasilNormalisasiAlternatifHarga = mutableListOf<Double>()
    var hasilNormalisasiAlternatifRating = mutableListOf<Double>()

    var hasilNormalisasiTerbobotJarak = mutableListOf<Double>()
    var hasilNormalisasiTerbobotHarga = mutableListOf<Double>()
    var hasilNormalisasiTerbobotRating = mutableListOf<Double>()

    var solusiPositifJarak = 0.0
    var solusiNegatifJarak = 0.0
    var solusiPositifHarga = 0.0
    var solusiNegatifHarga = 0.0
    var solusiPositifRating = 0.0
    var solusiNegatifRating = 0.0

    var hasilPerhitunganJarakIdealPositif = mutableListOf<Double>()
    var hasilPerhitunganJarakIdealNegatif = mutableListOf<Double>()

    var hasilAkhir = arrayListOf<Double>()
    var hasilAkhirRekomendasi = arrayListOf<ResultRecommendationEntity>()

    fun setBobot(price: Int, distance: Int, rating: Int) {
        bobotHarga = price
        bobotJarak = distance
        bobotRating = rating
    }

    fun setDataCoWorkingSpace(data: List<TopsisEntity>) {
        dataCoWorking = data
    }

    fun jumlahNilaiAlternatif() {
        //jarak
        var tmpJarak = 0.0
        for (i in dataCoWorking.indices) {
            tmpJarak += dataCoWorking[i].distance.pow(2.0)
        }
        jmlNilaiAlternatifJarak = sqrt(tmpJarak)

        //harga
        var tmpHarga = 0.0
        for (i in dataCoWorking.indices) {
            tmpHarga += dataCoWorking[i].price.toDouble().pow(2.0)
        }
        jmlNilaiAlternatifHarga = sqrt(tmpHarga)

        //rating
        var tmpRating = 0.0
        for (i in dataCoWorking.indices) {
            tmpRating += dataCoWorking[i].rating.pow(2.0)
        }
        jmlNilaiAlternatifRating = sqrt(tmpRating)
    }

    fun hasilNormalisasiAlternatif() {
        for (i in dataCoWorking.indices) {
            hasilNormalisasiAlternatifJarak.add(dataCoWorking[i].distance / jmlNilaiAlternatifJarak)
            hasilNormalisasiAlternatifHarga.add(dataCoWorking[i].price / jmlNilaiAlternatifHarga)
            hasilNormalisasiAlternatifRating.add(dataCoWorking[i].rating / jmlNilaiAlternatifRating)
        }
    }

    fun normalisasiTerbobot() {
        for (i in hasilNormalisasiAlternatifJarak.indices) {
            hasilNormalisasiTerbobotJarak.add(hasilNormalisasiAlternatifJarak[i] * bobotJarak)
        }
        for (i in hasilNormalisasiAlternatifHarga.indices) {
            hasilNormalisasiTerbobotHarga.add(hasilNormalisasiAlternatifHarga[i] * bobotHarga)
        }
        for (i in hasilNormalisasiAlternatifRating.indices) {
            hasilNormalisasiTerbobotRating.add(hasilNormalisasiAlternatifRating[i] * bobotRating)
        }
    }

    fun hasilIdealPositifdanNegatif() {
        solusiPositifJarak = Collections.max(hasilNormalisasiTerbobotJarak)
        solusiNegatifJarak = Collections.min(hasilNormalisasiTerbobotJarak)
        solusiPositifHarga = Collections.max(hasilNormalisasiTerbobotHarga)
        solusiNegatifHarga = Collections.min(hasilNormalisasiTerbobotHarga)
        solusiPositifRating = Collections.max(hasilNormalisasiTerbobotRating)
        solusiNegatifRating = Collections.min(hasilNormalisasiTerbobotRating)
    }

    fun hasilPerhitunganJarakIdealPositifdanNegatif() {
        for (i in hasilNormalisasiTerbobotJarak.indices) {
            //ideal positif
            hasilPerhitunganJarakIdealPositif.add(
                sqrt(
                    (hasilNormalisasiTerbobotJarak[i] - solusiPositifJarak).pow(2.0) + (hasilNormalisasiTerbobotHarga[i] - solusiPositifHarga).pow(
                        2.0
                    ) + (hasilNormalisasiTerbobotRating[i] - solusiPositifRating).pow(2.0)
                )
            )
            //ideal negatif
            hasilPerhitunganJarakIdealNegatif.add(
                sqrt(
                    (hasilNormalisasiTerbobotJarak[i] - solusiNegatifJarak).pow(2.0) + (hasilNormalisasiTerbobotHarga[i] - solusiNegatifHarga).pow(
                        2.0
                    ) + (hasilNormalisasiTerbobotRating[i] - solusiNegatifRating).pow(2.0)
                )
            )
        }
    }

    fun nilaiPreferensiSetiapAlternatif() {
        for (i in hasilPerhitunganJarakIdealNegatif.indices) {
            hasilAkhir.add(hasilPerhitunganJarakIdealNegatif[i] / (hasilPerhitunganJarakIdealNegatif[i] + hasilPerhitunganJarakIdealPositif[i]))
        }
    }

    fun petakanHasil() {
        for (i in hasilAkhir.indices) {
            hasilAkhirRekomendasi.add(ResultRecommendationEntity(i.toString(), hasilAkhir[i]))
        }
        hasilAkhirRekomendasi.sortBy {
            it.valueRecommendation
        }
    }

}