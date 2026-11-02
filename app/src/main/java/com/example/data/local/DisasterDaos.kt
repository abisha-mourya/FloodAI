package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.EmergencyContact
import com.example.data.model.FamilyMember
import com.example.data.model.ShelterItem
import com.example.data.model.SupplyItem
import com.example.data.model.WaterLevelLog
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyContactDao {
    @Query("SELECT * FROM emergency_contacts ORDER BY isPrimarySos DESC, name ASC")
    fun getAllContacts(): Flow<List<EmergencyContact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: EmergencyContact): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contacts: List<EmergencyContact>)

    @Update
    suspend fun updateContact(contact: EmergencyContact)

    @Delete
    suspend fun deleteContact(contact: EmergencyContact)
}

@Dao
interface FamilyMemberDao {
    @Query("SELECT * FROM family_members ORDER BY lastCheckInTime DESC")
    fun getAllMembers(): Flow<List<FamilyMember>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: FamilyMember): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(members: List<FamilyMember>)

    @Update
    suspend fun updateMember(member: FamilyMember)

    @Delete
    suspend fun deleteMember(member: FamilyMember)
}

@Dao
interface SupplyItemDao {
    @Query("SELECT * FROM supplies_checklist ORDER BY isLifeCritical DESC, category ASC, id ASC")
    fun getAllSupplies(): Flow<List<SupplyItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupply(item: SupplyItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<SupplyItem>)

    @Update
    suspend fun updateSupply(item: SupplyItem)

    @Delete
    suspend fun deleteSupply(item: SupplyItem)

    @Query("UPDATE supplies_checklist SET isChecked = :isChecked WHERE id = :id")
    suspend fun setChecked(id: Long, isChecked: Boolean)
}

@Dao
interface WaterLevelLogDao {
    @Query("SELECT * FROM water_level_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<WaterLevelLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: WaterLevelLog): Long

    @Delete
    suspend fun deleteLog(log: WaterLevelLog)

    @Query("DELETE FROM water_level_logs")
    suspend fun clearAll()
}

@Dao
interface ShelterDao {
    @Query("SELECT * FROM shelters ORDER BY elevationMeters DESC, name ASC")
    fun getAllShelters(): Flow<List<ShelterItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShelter(shelter: ShelterItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(shelters: List<ShelterItem>)

    @Update
    suspend fun updateShelter(shelter: ShelterItem)

    @Delete
    suspend fun deleteShelter(shelter: ShelterItem)

    @Query("DELETE FROM shelters")
    suspend fun clearAllShelters()
}
