/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfiso.util;

import com.sfiso.data.City;
import com.sfiso.data.Consultant;
import com.sfiso.data.Consultantorganisation;
import com.sfiso.data.Equipment;
import com.sfiso.data.Equipmentmanager;
import com.sfiso.data.Inventory;
import com.sfiso.data.Organisation;
import com.sfiso.data.User;
import com.sfiso.data.Userinventory;
import com.sfiso.dto.EquipmentDTO;
import com.sfiso.dto.InventoryDTO;
import com.sfiso.dto.ResponseDTO;
import com.sfiso.dto.UserinventoryDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author CodeTribe1
 */
public class DataUtil {

    private static final Logger LOG = Logger.getLogger(DataUtil.class.getSimpleName());
    private static EntityManager em;

    public static ResponseDTO assignDeviceToUser(UserinventoryDTO dto) throws DataException {
        em = EMUtil.getEntityManager();
        ResponseDTO resp = new ResponseDTO();
        EntityTransaction en = em.getTransaction();
        en.begin();
        Userinventory ui = new Userinventory();
        ui.setInventory(getInventory(dto.getInventoryID()));
        ui.setUser(getUser(dto.getUserID()));
        try {
            em.persist(ui);
            en.commit();
        } catch (ConstraintViolationException e) {
            throw new DataException(DataException.DUPLICATE);
        } catch (IllegalStateException e) {
            throw new DataException(DataException.ILLEGAL_STATE);
        } catch (Exception e) {
            throw new DataException(DataException.UNKNOWN_ERROR);
        } finally {
            if (en.isActive()) {
                en.rollback();
            }
            em.close();
        }
        return resp;
    }

    public static ResponseDTO addEquipment(EquipmentDTO dto) throws DataException {
        em = EMUtil.getEntityManager();
        ResponseDTO resp = new ResponseDTO();
        EntityTransaction en = em.getTransaction();
        en.begin();
        Equipment e = new Equipment();
        e.setEquipmentName(dto.getEquipmentName());
        e.setOrganisation(getOrganisation(dto.getOrganisationID()));
        try {
            em.persist(e);
            en.commit();
        } catch (ConstraintViolationException ex) {
            throw new DataException(DataException.DUPLICATE);
        } catch (IllegalStateException ex) {
            throw new DataException(DataException.ILLEGAL_STATE);
        } catch (Exception ex) {
            throw new DataException(DataException.UNKNOWN_ERROR);
        } finally {
            if (en.isActive()) {
                en.rollback();
            }
            em.close();
        }
        return resp;
    }

    public static ResponseDTO addInventory(InventoryDTO dto) throws DataException {
        em = EMUtil.getEntityManager();
        ResponseDTO resp = new ResponseDTO();
        EntityTransaction en = em.getTransaction();
        en.begin();
        Inventory e = new Inventory();
        e.setInventoryName(dto.getInventoryName());
        e.setInventoryModel(dto.getInventoryModel());
        e.setInventorySerialNo(dto.getInventorySerialNo());
        e.setEquipment(getEquipment(dto.getEquipmentID()));

        try {
            em.persist(e);
            en.commit();
        } catch (ConstraintViolationException ex) {
            throw new DataException(DataException.DUPLICATE);
        } catch (IllegalStateException ex) {
            throw new DataException(DataException.ILLEGAL_STATE);
        } catch (Exception ex) {
            throw new DataException(DataException.UNKNOWN_ERROR);
        } finally {
            if (en.isActive()) {
                en.rollback();
            }
            em.close();
        }
        return resp;
    }

    public static ResponseDTO getAllEquipments() throws DataException {
        em = EMUtil.getEntityManager();
        ResponseDTO resp = new ResponseDTO();

        try {
            Query q = em.createNamedQuery("Equipment.findAll");
            List<Equipment> eq = q.getResultList();
            List<EquipmentDTO> equipmentDTOs = new ArrayList<>();
            List<InventoryDTO> inventoryDTOs = getInventoryByEquiID();
            EquipmentDTO equipmentDTO;
            for (Equipment e : eq) {
                equipmentDTO = new EquipmentDTO(e);
                for (int i = 0; i < inventoryDTOs.size(); ++i) {
                    if (e.getEquipmentID() == inventoryDTOs.get(i).getEquipmentID()) {
                        equipmentDTO.getInventoryList().add(inventoryDTOs.get(i));
                    }
                }
                equipmentDTOs.add(equipmentDTO);                
            }
            resp.setEquipmentDTOs(equipmentDTOs);

        } catch (ConstraintViolationException ex) {
            throw new DataException(DataException.DUPLICATE);
        } catch (IllegalStateException ex) {
            throw new DataException(DataException.ILLEGAL_STATE);
        } catch (Exception ex) {
            throw new DataException(DataException.UNKNOWN_ERROR);
        }
        return resp;
    }

    public static List<InventoryDTO> getInventoryByEquiID() throws DataException {
        em = EMUtil.getEntityManager();
        List<InventoryDTO> inventoryDTOs = new ArrayList<>();
        try {
            Query q = em.createNamedQuery("Inventory.findAll");
            List<Inventory> in = q.getResultList();
            for (Inventory i : in) {
                inventoryDTOs.add(new InventoryDTO(i));
            }
        } catch (ConstraintViolationException ex) {
            throw new DataException(DataException.DUPLICATE);
        } catch (IllegalStateException ex) {
            throw new DataException(DataException.ILLEGAL_STATE);
        } catch (Exception ex) {
            throw new DataException(DataException.UNKNOWN_ERROR);
        }
        return inventoryDTOs;
    }

    //Entity helper methods
    public static Consultant getConsultant(int consultantID) {
        em = EMUtil.getEntityManager();
        Consultant c = em.find(Consultant.class, consultantID);
        return c;
    }

    public static Consultantorganisation getConsultantorganisation(int consultantorganisationID) {
        em = EMUtil.getEntityManager();
        Consultantorganisation c = em.find(Consultantorganisation.class, consultantorganisationID);
        return c;
    }

    public static Equipment getEquipment(int equipmentID) {
        em = EMUtil.getEntityManager();
        Equipment e = em.find(Equipment.class, equipmentID);
        return e;
    }

    public static Equipmentmanager getEquipmentmanager(int equipmentManagerID) {
        em = EMUtil.getEntityManager();
        Equipmentmanager c = em.find(Equipmentmanager.class, equipmentManagerID);
        return c;
    }

    public static Inventory getInventory(int inventoryID) {
        em = EMUtil.getEntityManager();
        Inventory c = em.find(Inventory.class, inventoryID);
        return c;
    }

    public static Organisation getOrganisation(int organisationID) {
        em = EMUtil.getEntityManager();
        Organisation c = em.find(Organisation.class, organisationID);
        return c;
    }

    public static User getUser(int userID) {
        em = EMUtil.getEntityManager();
        User c = em.find(User.class, userID);
        return c;
    }

    public static Userinventory getUserinventory(int userInventoryID) {
        em = EMUtil.getEntityManager();
        Userinventory c = em.find(Userinventory.class, userInventoryID);
        return c;
    }
}
