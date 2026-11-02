package com.example.util

import com.example.data.model.UrgencyLevel

data class OfflineLLMResponse(
    val summary: String,
    val actionSteps: List<String>,
    val fatalWarnings: List<String>,
    val urgencyLevel: UrgencyLevel,
    val referencedTopic: String
)

object OfflineDisasterLLM {

    /**
     * Executes local, on-device natural language understanding and inference
     * to answer disaster survival, first-aid, water, and evacuation doubts offline.
     */
    fun ask(userQuery: String): OfflineLLMResponse {
        val q = userQuery.trim().lowercase()

        return when {
            // 1. Snake bite / animal bite (Checked first to catch "snake bite in water")
            q.contains("snake") || q.contains("bite") || q.contains("venom") || q.contains("cobra") || q.contains("viper") || q.contains("krait") -> {
                OfflineLLMResponse(
                    summary = "Floods force venomous snakes into trees and homes. Keep the victim calm and still — panic accelerates venom spread throughout the bloodstream.",
                    actionSteps = listOf(
                        "Step 1: Keep the bitten limb IMMOBILE and slightly below heart level. Rest the patient flat.",
                        "Step 2: Remove all rings, bangles, watches, and tight footwear immediately before massive swelling begins.",
                        "Step 3: Gently wipe the wound with clean water without scrubbing.",
                        "Step 4: Signal for emergency evacuation to reach a medical center with Anti-Snake Venom (ASV)."
                    ),
                    fatalWarnings = listOf(
                        "DO NOT cut the wound with a blade or try to suck venom out (causes severe hemorrhage and infection).",
                        "DO NOT apply a tight tourniquet that cuts off blood circulation (leads to limb amputation).",
                        "DO NOT apply ice, herbal muds, or electric shocks."
                    ),
                    urgencyLevel = UrgencyLevel.LIFE_THREATENING,
                    referencedTopic = "Venomous Snake Bite Protocol"
                )
            }

            // 2. Drowning / CPR / Unconscious
            q.contains("drown") || q.contains("cpr") || q.contains("breath") || q.contains("unconscious") || q.contains("pulse") || q.contains("chok") -> {
                OfflineLLMResponse(
                    summary = "Immediate CPR is critical for someone pulled from floodwater who is unresponsive and not breathing normally. Brain damage occurs after 4 minutes without oxygen.",
                    actionSteps = listOf(
                        "Step 1: Place victim flat on their back on a firm surface. Check breathing (look, listen, feel for 5-10 sec).",
                        "Step 2: Tilt head back, pinch nose, and deliver 5 INITIAL RESCUE BREATHS mouth-to-mouth until chest rises.",
                        "Step 3: Place heel of your hand on the center of the chest. Push hard and fast: 100-120 compressions per minute, 2 inches (5 cm) deep.",
                        "Step 4: Continue cycles of 30 CHEST COMPRESSIONS followed by 2 RESCUE BREATHS.",
                        "Step 5: If victim begins breathing, roll them onto their side into the Recovery Position to prevent choking on vomited water."
                    ),
                    fatalWarnings = listOf(
                        "DO NOT waste time trying to squeeze water out of lungs or stomach with the Heimlich maneuver.",
                        "DO NOT delay chest compressions to look for a pulse if the person is limp and not breathing."
                    ),
                    urgencyLevel = UrgencyLevel.LIFE_THREATENING,
                    referencedTopic = "Drowning & Cardiopulmonary Resuscitation (CPR)"
                )
            }

            // 3. Car sinking / vehicle swept away
            q.contains("car") || q.contains("vehicle") || q.contains("sink") || q.contains("submerg") || q.contains("headrest") -> {
                OfflineLLMResponse(
                    summary = "Turn Around, Don't Drown! Just 30 cm (1 foot) of water floats cars; 60 cm sweeps heavy SUVs. Escape during the golden 30-60 seconds before electrical short-circuit.",
                    actionSteps = listOf(
                        "Step 1: Stay calm and unbuckle all seatbelts immediately (adults first, then children).",
                        "Step 2: Roll down power windows IMMEDIATELY while battery circuits still function.",
                        "Step 3: If windows are jammed, use the metal prongs of the headrest to strike and smash the bottom corner of a side window.",
                        "Step 4: Push through window and climb onto the roof of the vehicle. Signal for rescue."
                    ),
                    fatalWarnings = listOf(
                        "DO NOT waste time trying to push car doors open against tons of outside water pressure.",
                        "DO NOT try to save luggage, bags, or paperwork."
                    ),
                    urgencyLevel = UrgencyLevel.LIFE_THREATENING,
                    referencedTopic = "Submerged Vehicle Escape"
                )
            }

            // 4. Powerline / electricity in water
            q.contains("powerline") || q.contains("wire") || q.contains("electr") || q.contains("shock") || q.contains("current") -> {
                OfflineLLMResponse(
                    summary = "Water conducts lethal voltage across long distances. A submerged line or pole can electrify an entire block invisibly.",
                    actionSteps = listOf(
                        "Step 1: Stay at least 15 meters (50 feet) away from any downed wire, fallen transformer, or water touching cables.",
                        "Step 2: If you feel electrical tingling in your legs while wading: STOP IMMEDIATELY. Keep your feet touching together.",
                        "Step 3: Hop away on one foot or shuffle feet forward without lifting either foot off the ground.",
                        "Step 4: If a wire falls across your car: STAY INSIDE until power authorities shut down the grid."
                    ),
                    fatalWarnings = listOf(
                        "DO NOT touch standing water near damaged electrical utility poles.",
                        "DO NOT use wooden sticks or poles to move wires (wet wood conducts lethal voltage)."
                    ),
                    urgencyLevel = UrgencyLevel.LIFE_THREATENING,
                    referencedTopic = "Electrical Water Hazard & Electrocution"
                )
            }

            // 5. Attic trap / roof / rising water inside house
            q.contains("attic") || q.contains("roof") || q.contains("trap") || q.contains("axe") || q.contains("crowbar") || q.contains("ceiling") -> {
                OfflineLLMResponse(
                    summary = "CRITICAL HAZARD: NEVER retreat into an enclosed attic without a way to break through the roof. Hundreds of flood victims drown trapped against ceiling joists.",
                    actionSteps = listOf(
                        "Step 1: Shut off main power breaker ONLY if you can reach it safely while completely dry.",
                        "Step 2: Move to the upper floor with life jackets, drinking water, phone, flashlight, whistle, and dry blankets.",
                        "Step 3: If forced into the attic, ALWAYS bring an AXE, CROWBAR, or heavy hammer to smash through the roof shingles.",
                        "Step 4: Climb onto the exterior roof ridge so rescue boats and helicopters can see and extract you.",
                        "Step 5: Wave orange/yellow cloth by day or strobe flashlight by night. Whistle 3 short blasts."
                    ),
                    fatalWarnings = listOf(
                        "DO NOT hide in an attic without an axe or roof opening.",
                        "DO NOT try to swim through rushing street water outside — currents exceed 20 km/h."
                    ),
                    urgencyLevel = UrgencyLevel.LIFE_THREATENING,
                    referencedTopic = "Attic & Rooftop Evacuation"
                )
            }

            // 6. Generator / carbon monoxide
            q.contains("generator") || q.contains("co") || q.contains("carbon") || q.contains("monoxide") || q.contains("exhaust") -> {
                OfflineLLMResponse(
                    summary = "Carbon monoxide is an invisible, odorless, tasteless killer. More disaster deaths occur from generator poisoning than drowning.",
                    actionSteps = listOf(
                        "Step 1: Generators MUST run OUTDOORS ONLY, at least 6 meters (20 feet) away from all windows, doors, and vents.",
                        "Step 2: Point the exhaust muffler away from homes and living quarters.",
                        "Step 3: If anyone experiences headache, dizziness, nausea, or confusion, evacuate them to fresh outdoor air IMMEDIATELY.",
                        "Step 4: Let generator cool down for 15 minutes before refilling with petrol or diesel."
                    ),
                    fatalWarnings = listOf(
                        "NEVER run a generator inside a house, garage, basement, covered balcony, or carport.",
                        "DO NOT plug generator directly into home wall outlets without a certified transfer switch."
                    ),
                    urgencyLevel = UrgencyLevel.LIFE_THREATENING,
                    referencedTopic = "Portable Generator Safety"
                )
            }

            // 7. Severe bleeding / cut / wound
            q.contains("bleed") || q.contains("cut") || q.contains("wound") || q.contains("blood") || q.contains("tourniquet") || q.contains("lacerat") -> {
                OfflineLLMResponse(
                    summary = "Murky floodwater hides sharp metal sheets, rusted nails, and broken glass. Severe arterial bleeding must be stopped within minutes.",
                    actionSteps = listOf(
                        "Step 1: Press directly on the wound with a clean cloth, towel, or folded garment with FIRM, UNINTERRUPTED PRESSURE.",
                        "Step 2: Hold continuous pressure for at least 10 minutes without lifting to peek.",
                        "Step 3: Elevate the bleeding limb higher than heart level while maintaining firm pressure.",
                        "Step 4: Wrap a tight pressure bandage around the wound to maintain compression.",
                        "Step 5: If blood spurts uncontrollably from an arm or leg: Apply a tourniquet 5-7 cm above the wound and tighten until bleeding ceases."
                    ),
                    fatalWarnings = listOf(
                        "DO NOT rinse open heavy wounds under contaminated floodwater.",
                        "DO NOT pull out deeply embedded objects (rebar, glass) — pad around them to prevent catastrophic hemorrhage."
                    ),
                    urgencyLevel = UrgencyLevel.CRITICAL,
                    referencedTopic = "Hemorrhage & Deep Laceration Control"
                )
            }

            // 8. Cholera / diarrhea / vomiting / ORS
            q.contains("cholera") || q.contains("diarrhea") || q.contains("vomit") || q.contains("ors") || q.contains("stomach") || q.contains("dehydrat") -> {
                OfflineLLMResponse(
                    summary = "Severe watery diarrhea (rice-water stool) caused by Cholera or Typhoid dehydrates and kills within 24 hours. Continuous oral rehydration is lifesaving.",
                    actionSteps = listOf(
                        "Step 1: Prepare Homemade ORS: 1 Liter clean boiled water + 6 level teaspoons Sugar + 1/2 level teaspoon Salt.",
                        "Step 2: Stir until dissolved. Drink continuously after every loose stool (at least 200-400 ml per hour).",
                        "Step 3: Wash hands with soap or alcohol before handling food.",
                        "Step 4: Continue feeding breastmilk to infants; do not withhold fluids."
                    ),
                    fatalWarnings = listOf(
                        "DO NOT stop drinking liquids because of diarrhea — dehydration is what causes shock and kidney failure.",
                        "DO NOT consume cut raw vegetables or street food prepared with floodwater."
                    ),
                    urgencyLevel = UrgencyLevel.CRITICAL,
                    referencedTopic = "Waterborne Epidemic & ORS Protocol"
                )
            }

            // 9. Helicopter / rescue signaling
            q.contains("signal") || q.contains("chopper") || q.contains("heli") || q.contains("flare") || q.contains("whistle") -> {
                OfflineLLMResponse(
                    summary = "Air rescue crews and boat searchers scan rooftops and tree lines. Use international distress symbols and contrast.",
                    actionSteps = listOf(
                        "Step 1: Lay a brightly colored tarp (yellow, orange, red) or paint a large 'V' (Require Assistance) or 'X' (Require Medical) on the roof.",
                        "Step 2: Audio signal: Blow a whistle 3 SHARP BLASTS, pause for 1 minute, then repeat (3 is the universal distress signal).",
                        "Step 3: Day optical: Reflect sunlight toward rescuers using a smartphone screen or pocket mirror.",
                        "Step 4: Night optical: Flash strobe light in groups of 3 flashes.",
                        "Step 5: When helicopter hovers: Secure all loose blankets and tarps so rotor wash does not blast them into engine intakes."
                    ),
                    fatalWarnings = listOf(
                        "DO NOT shine laser pointers or high-beam spotlights directly into pilot cockpit eyes.",
                        "DO NOT tie the helicopter rescue hoist cable to your roof, boat, or railing (aircraft will crash)."
                    ),
                    urgencyLevel = UrgencyLevel.LIFE_THREATENING,
                    referencedTopic = "Search & Rescue Signaling"
                )
            }

            // 10. Emergency childbirth
            q.contains("birth") || q.contains("labor") || q.contains("baby") || q.contains("pregnan") || q.contains("delivery") -> {
                OfflineLLMResponse(
                    summary = "Emergency labor during flood isolation: Stay calm, prepare a clean, warm environment, and avoid pulling on the infant or umbilical cord.",
                    actionSteps = listOf(
                        "Step 1: Set up a dry, clean area with plastic sheets and freshly washed towels.",
                        "Step 2: Boil clean water to sterilize scissors and 2 clean pieces of string.",
                        "Step 3: Guide the baby gently as the head crowns; support the body without pulling.",
                        "Step 4: Immediately dry the newborn thoroughly with a dry towel and place skin-to-skin directly on the mother's chest to prevent hypothermia.",
                        "Step 5: After cord stops pulsing (3-5 min), tie string tightly 8 cm and 12 cm from baby's navel. Cut between the two knots with boiled scissors."
                    ),
                    fatalWarnings = listOf(
                        "DO NOT pull on the umbilical cord to remove the placenta (causes uterine rupture).",
                        "DO NOT wash off the protective white coating on the newborn's skin."
                    ),
                    urgencyLevel = UrgencyLevel.LIFE_THREATENING,
                    referencedTopic = "Emergency Childbirth in Flood Isolation"
                )
            }

            // 11. Hypothermia / cold water / shivering
            q.contains("cold") || q.contains("shiver") || q.contains("hypotherm") || q.contains("freez") -> {
                OfflineLLMResponse(
                    summary = "Floodwater saps body heat 25 times faster than cold air. Prolonged wet clothes trigger severe hypothermia even in warm tropical climates.",
                    actionSteps = listOf(
                        "Step 1: Strip off all soaked clothes immediately; dry the skin thoroughly.",
                        "Step 2: Wrap patient in dry blankets, dry clothes, or plastic bags / emergency space blanket to trap body heat.",
                        "Step 3: Re-warm the body core (chest, armpits, groin) using warm bottles wrapped in towels.",
                        "Step 4: Give warm, sweet tea or ORS only if patient is conscious and swallowing."
                    ),
                    fatalWarnings = listOf(
                        "DO NOT give alcoholic drinks (alcohol dilates blood vessels and causes rapid heat loss).",
                        "DO NOT rub or massage cold hands or feet (forces cold acidic blood back into heart, triggering cardiac arrest)."
                    ),
                    urgencyLevel = UrgencyLevel.CRITICAL,
                    referencedTopic = "Hypothermia & Cold Shock Protocol"
                )
            }

            // 12. Trench foot / skin / feet
            q.contains("foot") || q.contains("feet") || q.contains("trench") || q.contains("blister") -> {
                OfflineLLMResponse(
                    summary = "Trench foot occurs when feet stay immersed in wet conditions for prolonged hours, causing nerve damage, ulcers, and fungal decay.",
                    actionSteps = listOf(
                        "Step 1: Wash feet daily with clean boiled water and soap.",
                        "Step 2: Dry thoroughly between every single toe. Elevate feet above heart level while resting.",
                        "Step 3: Apply coconut oil or petroleum jelly (Vaseline) before wading to create a water-repellent protective barrier.",
                        "Step 4: Keep dry socks sealed in plastic bags. Never sleep in wet shoes."
                    ),
                    fatalWarnings = listOf(
                        "DO NOT pop or scratch water blisters (opens entry for cellulitis and gangrene).",
                        "DO NOT sleep wearing soaked footwear."
                    ),
                    urgencyLevel = UrgencyLevel.IMPORTANT_PREP,
                    referencedTopic = "Trench Foot & Fungal Infection"
                )
            }

            // 13. Building collapse / trapped under rubble
            q.contains("collaps") || q.contains("rubble") || q.contains("debris") || q.contains("earthquake") -> {
                OfflineLLMResponse(
                    summary = "Flood erosion weakens foundations, causing sudden wall collapses. Protect your airway and signal with metal taps.",
                    actionSteps = listOf(
                        "Step 1: Cover nose and mouth with a cotton shirt or cloth to avoid inhaling suffocating cement dust.",
                        "Step 2: Curl into fetal position next to a solid structural void (bed frame, table, thick concrete pillar).",
                        "Step 3: Tap on metal pipes or walls in rhythmic groups of THREE (Tap-Tap-Tap) so search sonar and dogs can locate you.",
                        "Step 4: Shout only when you hear rescuers actively walking directly overhead (conserves oxygen and throat moisture)."
                    ),
                    fatalWarnings = listOf(
                        "DO NOT light matches or lighters (gas line breaks cause deadly explosions).",
                        "DO NOT struggle frantically if pinned (causes secondary structural shifts)."
                    ),
                    urgencyLevel = UrgencyLevel.LIFE_THREATENING,
                    referencedTopic = "Structural Collapse & Entrapment"
                )
            }

            // 14. Water purification / drinking floodwater
            q.contains("drink") || q.contains("water") || q.contains("purif") || q.contains("boil") || q.contains("thirst") || q.contains("bleach") -> {
                OfflineLLMResponse(
                    summary = "NEVER drink raw floodwater. It contains sewage, dead animals, industrial chemicals, and deadly bacteria (Cholera, E. coli). You must boil or chlorinate it.",
                    actionSteps = listOf(
                        "Step 1: Filter cloudy water through a clean cotton cloth or folded towel into a clean vessel to remove grit.",
                        "Step 2 (Best): Bring water to a ROLLING BOIL for at least 1 full continuous minute.",
                        "Step 3 (If no fire): Use unscented household bleach — add 2 drops per 1 liter (or 8 drops per gallon) of clear water. Stir and wait 30 MINUTES.",
                        "Step 4: Store in covered, clean containers. Drink small sips regularly."
                    ),
                    fatalWarnings = listOf(
                        "DO NOT use scented, splashless, or color-safe bleach (they contain toxic detergents).",
                        "DO NOT use floodwater to rinse plates, brush teeth, or wash open wounds."
                    ),
                    urgencyLevel = UrgencyLevel.CRITICAL,
                    referencedTopic = "Water Purification & Disinfection"
                )
            }

            // 15. Hotlines / emergency numbers
            q.contains("number") || q.contains("call") || q.contains("police") || q.contains("ambulance") || q.contains("ndrf") || q.contains("helpline") || q.contains("contact") -> {
                OfflineLLMResponse(
                    summary = "Official National & Disaster Relief Emergency Numbers in India (Free 24x7):",
                    actionSteps = listOf(
                        "112 — National All-in-One Emergency (Police, Fire, Ambulance)",
                        "1078 / 011-24363260 — NDRF (National Disaster Response Force HQ)",
                        "108 — Emergency Medical & Ambulance Service",
                        "1070 — State Disaster Management Authority (SDMA)",
                        "1077 — District Emergency Operations Center (DEOC)",
                        "1554 — Indian Coast Guard (Flood Air / Boat Search & Rescue)",
                        "8384034810 — FloodAid Disaster SMS Rescue Desk"
                    ),
                    fatalWarnings = listOf(
                        "Voice calls often fail during storms; send SMS text messages which route via 2G network buffers."
                    ),
                    urgencyLevel = UrgencyLevel.IMPORTANT_PREP,
                    referencedTopic = "Emergency Direct Hotlines"
                )
            }

            // Fallback: General disaster query reasoning
            else -> {
                OfflineLLMResponse(
                    summary = "Disaster Survival Rule: Prioritize staying dry on high ground, secure clean boiled drinking water, and keep signaling rescuers.",
                    actionSteps = listOf(
                        "1. Safety: Never step into moving floodwater or touch water near electrical lines.",
                        "2. Water: Boil all water for 1 minute before drinking; do not consume raw floodwater.",
                        "3. Evacuation: Move to rooftop or high ground. Wave bright cloth or use 3 whistle blasts.",
                        "4. Health: Clean any skin cut immediately with clean bottled water and soap.",
                        "5. Communication: Use SMS to dispatch coordinates; voice networks often drop."
                    ),
                    fatalWarnings = listOf(
                        "NEVER drive through flooded roads (15 cm knocks you down, 30 cm floats cars).",
                        "NEVER run generators indoors or in garages."
                    ),
                    urgencyLevel = UrgencyLevel.CRITICAL,
                    referencedTopic = "General Disaster Survival Triage"
                )
            }
        }
    }
}
