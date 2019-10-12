package net.bankcraft

sealed class ReviewType {
    object Imageless : ReviewType()
    class HasImage(val imgUrl: String) : ReviewType()

    companion object {
        fun fromModel(revieModel: ReviewModel): ReviewType {
            val url = revieModel.imageUrl

            return if (url != null) {
                HasImage(url)
            } else {
                Imageless
            }
        }
    }
}

data class ReviewModel(val imageUrl: String?)

class ReviewPresenter(initialModel: ReviewModel) {

    private var reviewType: ReviewType =
            ReviewType.fromModel(initialModel)

    fun setModel(updatedModel: ReviewModel) {
        reviewType = ReviewType.fromModel(updatedModel)
    }
}