package presentation

object Strings {
    fun language(lan: String): String {
        return when (lan) {
            "tr" -> "dil"
            "ru" -> "язык"
            else -> "language"
        }
    }

    fun easy(lan: String): String {
        return when (lan) {
            "tr" -> "kolay"
            "ru" -> "легкий"
            else -> "easy"
        }
    }

    fun medium(lan: String): String {
        return when (lan) {
            "tr" -> "orta"
            "ru" -> "средний"
            else -> "medium"
        }
    }

    fun hard(lan: String): String {
        return when (lan) {
            "tr" -> "zor"
            "ru" -> "тяжелый"
            else -> "hard"
        }
    }

    fun category(lan: String): String {
        return when (lan) {
            "tr" -> "kategori"
            "ru" -> "категория"
            else -> "category"
        }
    }

    fun difficulty(lan: String): String {
        return when (lan) {
            "tr" -> "seviye"
            "ru" -> "уровень"
            else -> "difficulty"
        }
    }

    fun play(lan: String): String {
        return when (lan) {
            "tr" -> "başla"
            "ru" -> "играть"
            else -> "play"
        }
    }

    fun playAgain(lan: String): String {
        return when (lan) {
            "tr" -> "tekrar oyna"
            "ru" -> "сыграй еще раз"
            else -> "play again"
        }
    }

    fun incorrect(lan: String): String {
        return when (lan) {
            "tr" -> "yanlış"
            "ru" -> "попытки"
            else -> "incorrect"
        }
    }

    fun questions(lan: String): String {
        return when (lan) {
            "tr" -> "sorular"
            "ru" -> "вопросы"
            else -> "questions"
        }
    }

    fun yes(lan: String): String {
        return when (lan) {
            "tr" -> "evet"
            "ru" -> "да"
            else -> "yes"
        }
    }

    fun noText(lan: String): String {
        return when (lan) {
            "tr" -> "hayır"
            "ru" -> "нет"
            else -> "no"
        }
    }

    fun invalidText(lan: String): String {
        return when (lan) {
            "tr" -> "geçersiz"
            "ru" -> "invalid"
            else -> "invalid"
        }
    }

    fun ask(lan: String): String {
        return when (lan) {
            "tr" -> "sor"
            "ru" -> "просить"
            else -> "ask"
        }
    }

    fun askHint(lan: String): String {
        return when (lan) {
            "tr" -> "Bu ... ?"
            "ru" -> "Это ... ?"
            else -> "Is this ... ?"
        }
    }

    fun lose(lan: String): String {
        return when (lan) {
            "tr" -> "KAYBETTİN"
            "ru" -> "ПРОИГРАЛ"
            else -> "LOSE"
        }
    }

    fun win(lan: String): String {
        return when (lan) {
            "tr" -> "KAZANDIN"
            "ru" -> "ПОБЕДИЛ"
            else -> "WIN"
        }
    }
}