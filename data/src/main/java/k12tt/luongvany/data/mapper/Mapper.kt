package k12tt.luongvany.data.mapper

internal interface Mapper<I, O> {
    fun map(source: I): O
}