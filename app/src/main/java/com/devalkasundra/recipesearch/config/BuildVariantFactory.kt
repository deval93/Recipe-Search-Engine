package com.devalkasundra.recipesearch.config

class BuildVariantFactory {
    companion object {
        private const val baseURL = "www.recipepuppy.com"

        fun getBaseURL(): String {
            return "http://$baseURL/"
        }
    }
}