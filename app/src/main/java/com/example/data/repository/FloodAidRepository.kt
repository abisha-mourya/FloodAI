package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.EmergencyContactDao
import com.example.data.local.FamilyMemberDao
import com.example.data.local.ShelterDao
import com.example.data.local.SupplyItemDao
import com.example.data.local.WaterLevelLogDao
import com.example.data.model.EmergencyContact
import com.example.data.model.FamilyMember
import com.example.data.model.ShelterItem
import com.example.data.model.SupplyItem
import com.example.data.model.WaterLevelLog
import kotlinx.coroutines.flow.Flow

class FloodAidRepository(private val database: AppDatabase) {

    private val contactDao: EmergencyContactDao = database.emergencyContactDao()
    private val memberDao: FamilyMemberDao = database.familyMemberDao()
    private val supplyDao: SupplyItemDao = database.supplyItemDao()
    private val waterLogDao: WaterLevelLogDao = database.waterLevelLogDao()
    private val shelterDao: ShelterDao = database.shelterDao()

    // Contacts
    val allContacts: Flow<List<EmergencyContact>> = contactDao.getAllContacts()
    suspend fun insertContact(contact: EmergencyContact) = contactDao.insertContact(contact)
    suspend fun updateContact(contact: EmergencyContact) = contactDao.updateContact(contact)
    suspend fun deleteContact(contact: EmergencyContact) = contactDao.deleteContact(contact)

    // Family Check-ins
    val allMembers: Flow<List<FamilyMember>> = memberDao.getAllMembers()
    suspend fun insertMember(member: FamilyMember) = memberDao.insertMember(member)
    suspend fun updateMember(member: FamilyMember) = memberDao.updateMember(member)
    suspend fun deleteMember(member: FamilyMember) = memberDao.deleteMember(member)

    // Supplies
    val allSupplies: Flow<List<SupplyItem>> = supplyDao.getAllSupplies()
    suspend fun insertSupply(item: SupplyItem) = supplyDao.insertSupply(item)
    suspend fun updateSupply(item: SupplyItem) = supplyDao.updateSupply(item)
    suspend fun setSupplyChecked(id: Long, isChecked: Boolean) = supplyDao.setChecked(id, isChecked)
    suspend fun deleteSupply(item: SupplyItem) = supplyDao.deleteSupply(item)

    // Water level logs
    val allWaterLogs: Flow<List<WaterLevelLog>> = waterLogDao.getAllLogs()
    suspend fun insertWaterLog(log: WaterLevelLog) = waterLogDao.insertLog(log)
    suspend fun deleteWaterLog(log: WaterLevelLog) = waterLogDao.deleteLog(log)

    // Shelters
    val allShelters: Flow<List<ShelterItem>> = shelterDao.getAllShelters()
    suspend fun insertShelter(shelter: ShelterItem) = shelterDao.insertShelter(shelter)
    suspend fun insertAllShelters(shelters: List<ShelterItem>) = shelterDao.insertAll(shelters)
    suspend fun updateShelter(shelter: ShelterItem) = shelterDao.updateShelter(shelter)
    suspend fun deleteShelter(shelter: ShelterItem) = shelterDao.deleteShelter(shelter)
    suspend fun clearAllShelters() = shelterDao.clearAllShelters()
    suspend fun replaceShelters(shelters: List<ShelterItem>) {
        shelterDao.clearAllShelters()
        shelterDao.insertAll(shelters)
    }
}
