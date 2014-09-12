/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfiso.data;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author CodeTribe1
 */
@Entity
@Table(name = "userinventory")
@NamedQueries({
    @NamedQuery(name = "Userinventory.findAll", query = "SELECT u FROM Userinventory u"),
    @NamedQuery(name = "Userinventory.findByUserInventoryID", query = "SELECT u FROM Userinventory u WHERE u.userInventoryID = :userInventoryID")})
public class Userinventory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "userInventoryID")
    private Integer userInventoryID;
    @JoinColumn(name = "inventoryID", referencedColumnName = "inventoryID")
    @ManyToOne(optional = false)
    private Inventory inventory;
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    @ManyToOne(optional = false)
    private User user;

    public Userinventory() {
    }

    public Userinventory(Integer userInventoryID) {
        this.userInventoryID = userInventoryID;
    }

    public Integer getUserInventoryID() {
        return userInventoryID;
    }

    public void setUserInventoryID(Integer userInventoryID) {
        this.userInventoryID = userInventoryID;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userInventoryID != null ? userInventoryID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Userinventory)) {
            return false;
        }
        Userinventory other = (Userinventory) object;
        if ((this.userInventoryID == null && other.userInventoryID != null) || (this.userInventoryID != null && !this.userInventoryID.equals(other.userInventoryID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sfiso.data.Userinventory[ userInventoryID=" + userInventoryID + " ]";
    }

}
