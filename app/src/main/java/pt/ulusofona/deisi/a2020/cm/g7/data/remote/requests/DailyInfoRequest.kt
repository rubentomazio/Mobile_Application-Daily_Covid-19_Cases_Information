package pt.ulusofona.deisi.a2020.cm.g7.data.remote.requests

data class DailyInfoRequest(val date: String, val newConfirmed: Int, val active: Int, val confirmed: Int,
                            val recovered: Int, val hospitalized: Int, val UCIhospitalized: Int, val totalDeaths: Int,
                            val rt: Int)