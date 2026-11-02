package com.example.data.local

import com.example.data.model.EmergencyContact
import com.example.data.model.FamilyMember
import com.example.data.model.ShelterItem
import com.example.data.model.SupplyItem
import com.example.data.model.UrgencyLevel
import com.example.data.model.WaterLevelLog

object PreloadedData {

    val defaultContacts = listOf(
        EmergencyContact(
            name = "National Emergency Helpline (NERSS)",
            phone = "112",
            category = "All-in-One Emergency",
            notes = "Police, Fire, Ambulance & Disaster Response across India.",
            isPrimarySos = true
        ),
        EmergencyContact(
            name = "NDRF Disaster Rescue Control Room",
            phone = "1078",
            category = "Disaster Rescue",
            notes = "National Disaster Response Force HQ: 1078 / 011-24363260.",
            isPrimarySos = true
        ),
        EmergencyContact(
            name = "Emergency Medical / Ambulance",
            phone = "108",
            category = "Medical Ambulance",
            notes = "24x7 Free Emergency Ambulance & Trauma Care.",
            isPrimarySos = true
        ),
        EmergencyContact(
            name = "State Disaster Control Room (SDMA)",
            phone = "1070",
            category = "State Disaster Relief",
            notes = "State Emergency Operations Center for flood relief boats.",
            isPrimarySos = false
        ),
        EmergencyContact(
            name = "District Emergency Operations (DEOC)",
            phone = "1077",
            category = "District Relief",
            notes = "District Magistrate / Collectorate flood relief cell.",
            isPrimarySos = false
        ),
        EmergencyContact(
            name = "Indian Coast Guard SAR (Coastal & Flood)",
            phone = "1554",
            category = "Water Rescue",
            notes = "Maritime & flooded river air/boat search & rescue.",
            isPrimarySos = true
        ),
        EmergencyContact(
            name = "FloodAid Direct SMS Rescue Desk",
            phone = "8384034810",
            category = "Direct Rescue SMS",
            notes = "Dedicated SMS Rescue Desk for victim status dispatch.",
            isPrimarySos = true
        )
    )

    val defaultSupplies = listOf(
        SupplyItem(
            title = "Drinking Water (1 Gallon / Person / Day)",
            category = "Water & Food",
            quantityDetail = "Minimum 3-day supply (72h) stored in food-grade clean jugs",
            isChecked = false,
            isLifeCritical = true
        ),
        SupplyItem(
            title = "Unscented Household Bleach (6%) & Dropper",
            category = "Water & Food",
            quantityDetail = "For emergency water purification (6-8 drops per gallon)",
            isChecked = false,
            isLifeCritical = true
        ),
        SupplyItem(
            title = "Non-Perishable Canned Foods & Manual Can Opener",
            category = "Water & Food",
            quantityDetail = "Ready-to-eat tuna, peanut butter, granola, beans",
            isChecked = false,
            isLifeCritical = true
        ),
        SupplyItem(
            title = "Axe, Crowbar or Heavy Hammer in Attic",
            category = "Tools & Safety",
            quantityDetail = "CRITICAL: If escaping rising water to attic, must bring tool to break roof!",
            isChecked = false,
            isLifeCritical = true
        ),
        SupplyItem(
            title = "Emergency Whistle & Waterproof Strobe",
            category = "Tools & Safety",
            quantityDetail = "Audible 300m+ to attract rescue boats in dark or attic",
            isChecked = false,
            isLifeCritical = true
        ),
        SupplyItem(
            title = "7-Day Prescription Medications & Insulin Cold Pack",
            category = "First Aid & Meds",
            quantityDetail = "Sealed in double Ziploc waterproof bags",
            isChecked = false,
            isLifeCritical = true
        ),
        SupplyItem(
            title = "First Aid Kit (Tourniquet, Antiseptic, Bandages)",
            category = "First Aid & Meds",
            quantityDetail = "Treat cuts from debris in dirty floodwater promptly",
            isChecked = false,
            isLifeCritical = true
        ),
        SupplyItem(
            title = "Fully Charged Power Banks & Waterproof Phone Pouches",
            category = "Power & Comm",
            quantityDetail = "2x 10,000mAh banks kept in airtight dry bags",
            isChecked = false,
            isLifeCritical = false
        ),
        SupplyItem(
            title = "Battery or Hand-Crank NOAA Weather Radio",
            category = "Power & Comm",
            quantityDetail = "Receives civil defense alerts when cellular towers collapse",
            isChecked = false,
            isLifeCritical = false
        ),
        SupplyItem(
            title = "Waterproof Document Bag (IDs, Deeds, Insurance, Cash)",
            category = "Documents",
            quantityDetail = "Cash in small bills ($1, $5, $20) as ATMs lose power",
            isChecked = false,
            isLifeCritical = false
        ),
        SupplyItem(
            title = "Heavy-Duty Trash Bags, Moist Towelettes & Bleach",
            category = "Sanitation",
            quantityDetail = "Municipal sewage fails during flood events; bucket toilet supplies",
            isChecked = false,
            isLifeCritical = false
        )
    )

    val defaultShelters = emptyList<ShelterItem>()

    val defaultFamily = listOf(
        FamilyMember(
            name = "Myself (Device Owner)",
            status = "Safe",
            lastKnownLocation = "Home - 2nd Floor",
            batteryPercent = 88,
            medicalNeeds = "Asthma Inhaler",
            bloodType = "O+",
            lastCheckInTime = System.currentTimeMillis()
        ),
        FamilyMember(
            name = "Grandmother (Elena)",
            status = "Safe",
            lastKnownLocation = "Highland Ridge Shelter",
            batteryPercent = 65,
            medicalNeeds = "Blood pressure meds, mobility cane",
            bloodType = "A+",
            lastCheckInTime = System.currentTimeMillis() - 7200000
        )
    )

    val initialWaterLogs = listOf(
        WaterLevelLog(
            timestamp = System.currentTimeMillis() - 10800000,
            depthInches = 4f,
            locationTag = "Driveway Curb",
            trend = "Rising Slowly",
            notes = "Water creeping past street gutter."
        ),
        WaterLevelLog(
            timestamp = System.currentTimeMillis() - 3600000,
            depthInches = 14f,
            locationTag = "Porch Step 1",
            trend = "Rising Rapidly",
            notes = "Heavy rain band hitting; sub-pump submerged."
        )
    )
}
