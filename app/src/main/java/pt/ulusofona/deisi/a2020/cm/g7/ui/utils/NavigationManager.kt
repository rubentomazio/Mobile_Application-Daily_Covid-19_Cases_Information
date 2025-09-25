package pt.ulusofona.deisi.a2020.cm.g7.ui.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import pt.ulusofona.deisi.a2020.cm.g7.R
import pt.ulusofona.deisi.a2020.cm.g7.ui.fragments.*

// Lida com a navegação
class NavigationManager {

    companion object {

        private fun placeFragment(fm: FragmentManager, fragment: Fragment) {
            val transition = fm.beginTransaction()
            transition.replace(R.id.frame, fragment)
            transition.addToBackStack(null)
            transition.commit()
        }

        fun goToDailyInfoFragment(fm: FragmentManager) {
            val fragment = DailyInfoFragment()
            placeFragment(fm, fragment)
        }

        fun goToCovidTestsFragment(fm: FragmentManager) {
            val fragment = CovidTestsListFragment()
            placeFragment(fm, fragment)
        }

        fun goToContactsFragment(fm: FragmentManager) {
            val fragment = ContactsFragment()
            placeFragment(fm, fragment)
        }

        fun goToVaccineInfoFragment(fm: FragmentManager) {
            val fragment = VaccineInfoFragment()
            placeFragment(fm, fragment)
        }

        fun goToCovidTestFormFragment(fm: FragmentManager, args: Bundle?) {
            val fragment = CovidTestFormFragment()
            args?.let { fragment.arguments = it }
            placeFragment(fm, fragment)
        }

        fun goToSettingsFragment(fm: FragmentManager) {
            val fragment = SettingsFragment()
            placeFragment(fm, fragment)
        }

        fun goToCountieSearchFragment(fm:FragmentManager){
            placeFragment(fm,CountieSearchFragment())
        }

        fun goToCountiesFragment(fm: FragmentManager) {
            placeFragment(fm, CountieFragment())
        }
    }
}