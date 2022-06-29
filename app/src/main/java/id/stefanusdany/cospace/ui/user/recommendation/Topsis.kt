package id.stefanusdany.cospace.ui.user.recommendation

import java.util.Collections
import kotlin.math.pow
import kotlin.math.sqrt
import android.util.Log
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
        Log.e("teaa", "jumlahNilaiAlternatifJarak: $jmlNilaiAlternatifJarak", )

        //harga
        var tmpHarga = 0.0
        for (i in dataCoWorking.indices) {
            tmpHarga += dataCoWorking[i].price.toDouble().pow(2.0)
        }
        jmlNilaiAlternatifHarga = sqrt(tmpHarga)
        Log.e("teaa", "jumlahNilaiAlternatifHarga: $jmlNilaiAlternatifHarga", )

        //rating
        var tmpRating = 0.0
        for (i in dataCoWorking.indices) {
            tmpRating += dataCoWorking[i].rating.pow(2.0)
        }
        jmlNilaiAlternatifRating = sqrt(tmpRating)
        Log.e("teaa", "jumlahNilaiAlternatifRating: $jmlNilaiAlternatifRating", )
    }

    fun hasilNormalisasiAlternatif() {
        for (i in dataCoWorking.indices) {
            hasilNormalisasiAlternatifJarak.add(dataCoWorking[i].distance / jmlNilaiAlternatifJarak)
            hasilNormalisasiAlternatifHarga.add(dataCoWorking[i].price / jmlNilaiAlternatifHarga)
            hasilNormalisasiAlternatifRating.add(dataCoWorking[i].rating / jmlNilaiAlternatifRating)
        }
        Log.e("teaa", "hasilNormalisasiAlternatifJarak: $hasilNormalisasiAlternatifJarak", )
        Log.e("teaa", "hasilNormalisasiAlternatifHarga: $hasilNormalisasiAlternatifHarga", )
        Log.e("teaa", "hasilNormalisasiAlternatifRating: $hasilNormalisasiAlternatifRating", )
    }

    fun normalisasiTerbobot() {
        for (i in hasilNormalisasiAlternatifJarak.indices) {
            hasilNormalisasiTerbobotJarak.add(hasilNormalisasiAlternatifJarak[i] * bobotJarak)
        }
        Log.e("teaa", "normalisasiTerbobotJarak: $hasilNormalisasiTerbobotJarak", )
        for (i in hasilNormalisasiAlternatifHarga.indices) {
            hasilNormalisasiTerbobotHarga.add(hasilNormalisasiAlternatifHarga[i] * bobotHarga)
        }
        Log.e("teaa", "normalisasiTerbobotHarga: $hasilNormalisasiTerbobotHarga", )
        for (i in hasilNormalisasiAlternatifRating.indices) {
            hasilNormalisasiTerbobotRating.add(hasilNormalisasiAlternatifRating[i] * bobotRating)
        }
        Log.e("teaa", "normalisasiTerbobotRating: $hasilNormalisasiTerbobotRating", )
    }

    fun hasilIdealPositifdanNegatif() {
        solusiPositifJarak = hasilNormalisasiTerbobotJarak.maxOrNull()?:0.0
        solusiNegatifJarak = hasilNormalisasiTerbobotJarak.minOrNull()?:0.0
        solusiPositifHarga = hasilNormalisasiTerbobotHarga.maxOrNull()?:0.0
        solusiNegatifHarga = hasilNormalisasiTerbobotHarga.minOrNull()?:0.0
        solusiPositifRating = hasilNormalisasiTerbobotRating.maxOrNull()?:0.0
        solusiNegatifRating = hasilNormalisasiTerbobotRating.minOrNull()?:0.0
        Log.e("teaa", "hasilIdealPositifdanNegatifJarak: $solusiPositifJarak + $solusiNegatifJarak", )
        Log.e("teaa", "hasilIdealPositifdanNegatifHarga: $solusiPositifHarga + $solusiNegatifHarga", )
        Log.e("teaa", "hasilIdealPositifdanNegatifRating: $solusiPositifRating + $solusiNegatifRating", )
    }

    fun hasilPerhitunganJarakIdealPositifdanNegatif() {
        for (i in hasilNormalisasiTerbobotJarak.indices) {
            //ideal positif
            hasilPerhitunganJarakIdealPositif.add(
                sqrt(
                    ((hasilNormalisasiTerbobotJarak[i] - solusiPositifJarak).pow(2.0)) + ((hasilNormalisasiTerbobotHarga[i] - solusiPositifHarga).pow(
                        2.0
                    )) + ((hasilNormalisasiTerbobotRating[i] - solusiPositifRating).pow(2.0))
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
        Log.e("teaa", "hasilPerhitunganJarakIdealPositif: $hasilPerhitunganJarakIdealPositif", )
        Log.e("teaa", "hasilPerhitunganJarakIdealNegatif: $hasilPerhitunganJarakIdealNegatif", )
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
        Log.e("teaa", "petakanHasil: $hasilAkhirRekomendasi", )
//        Log.e("bobot", "jarak: $bobotJarak, rating: $bobotRating, ", )
        if (bobotRating == 5 && bobotHarga == 0 && bobotJarak == 0) {
            hasilAkhirRekomendasi.sortByDescending {
                it.valueRecommendation
            }
        } else {
            hasilAkhirRekomendasi.sortBy {
                it.valueRecommendation
            }
        }
//        hasilAkhirRekomendasi.sortBy {
//            it.valueRecommendation
//        }

        Log.e("teaa", "petakanHasil2: $hasilAkhirRekomendasi", )
    }

}