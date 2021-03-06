/*
 * Balie - BAseLine Information Extraction
 * Copyright (C) 2004-2007  David Nadeau
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on Sept 21, 2006
 */
package ca.uottawa.balie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;

/**
 * Type of a NE
 * 
 * @author David Nadeau (pythonner@gmail.com)
 */
public class NamedEntityType implements Serializable, Cloneable {

    private static final long serialVersionUID = 2L;

    /**
     * Construct a new, empty, entity type
     * @param pi_TagSetSize size of NE tag set
     */
    public NamedEntityType(int pi_TagSetSize, NamedEntityExplanation pi_Info) {
        m_TagSetSize = pi_TagSetSize;
        m_BitValue = new BitSet(pi_TagSetSize);
        m_StrInfo = new StringBuffer();
        AddNewInfo(pi_Info);
    }

    /**
     * Construct a new entity type and set its type to a primitive
     * @param pi_Type primitive type
     */
    public NamedEntityType(NamedEntityTypeEnumI pi_Type, int pi_TagSetSize, NamedEntityExplanation pi_Info) {
        m_TagSetSize = pi_TagSetSize;
        m_BitValue = new BitSet(m_TagSetSize);
        m_StrInfo = new StringBuffer();
        AddType(pi_Type, pi_Info);
    }

    private NamedEntityType(BitSet pi_BitSet, int pi_TagSetSize, StringBuffer pi_Info) {
        m_TagSetSize = pi_TagSetSize;
        m_BitValue = (BitSet)pi_BitSet.clone();
        m_StrInfo = new StringBuffer();
        m_StrInfo.append(pi_Info);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public NamedEntityType clone(NamedEntityExplanation pi_Info) {
        AddNewInfo(pi_Info);
        NamedEntityType ne = new NamedEntityType(m_BitValue, m_TagSetSize, m_StrInfo);
        return ne;
    }
    
    /**
     * Add a primitive type to this entity type (e.g. add LOCATION)
     * An entity can have multiple types
     * @param pi_Type primitive type
     */
    public void AddType(NamedEntityTypeEnumI pi_Type, NamedEntityExplanation pi_Info) {
        m_BitValue.set(pi_Type.BitPos());
        AddNewInfo(pi_Info);
    }

    /**
     * Remove a primitive type from this entity
     * Succeed even if this entity do not have this type
     * @param pi_Type primitive type
     */
    public void RemoveType(NamedEntityTypeEnumI pi_Type, NamedEntityExplanation pi_Info) {
        m_BitValue.clear(pi_Type.BitPos());
        AddNewInfo(pi_Info);
    }

    /**
     * Check types that are common to this entity and the parameter entity
     * @param pi_NE an entity
     * @return true if there is a non empty intersection
     */
    public boolean Intersect(NamedEntityType pi_NE) {
        return m_BitValue.intersects(pi_NE.m_BitValue);
    }
    
    /**
     * Returns a entity that is the intersection of the two parameters
     * 
     * @param pi_NE1 an entity
     * @param pi_NE2 another entity
     * @return new entity that is the intersection of NE1 and NE2
     */
    public static NamedEntityType Intersection(NamedEntityType pi_NE1, NamedEntityType pi_NE2, NamedEntityExplanation pi_Info) {
        StringBuffer newInfo = new StringBuffer();
        newInfo.append(pi_NE1.m_StrInfo);
        newInfo.append(pi_NE2.m_StrInfo);
        if (pi_Info != null) newInfo.append("<li>"+pi_Info.toString()+"</li>");
        NamedEntityType newNE = new NamedEntityType((BitSet)pi_NE1.m_BitValue.clone(), pi_NE1.m_TagSetSize, newInfo);
        newNE.m_BitValue.and(pi_NE2.m_BitValue);
        return newNE;
    }
    
    /**
     * Keeps only the primitive types whose correspond to a type in the parameter entity
     * Equivalent to bitwise AND
     * @param pi_NE an entity
     */
    public void KeepOnly(NamedEntityType pi_NE, NamedEntityExplanation pi_Info) {
        m_BitValue.and(pi_NE.m_BitValue);
        AddNewInfo(pi_Info);
    }
    
    /**
     * Merge primitive type of current and parameter entity
     * Equivalent to bitwise OR
     * @param pi_NE an entity to merge with
     */
    public void MergeWith(NamedEntityType pi_NE, NamedEntityExplanation pi_Info) {
        m_BitValue.or(pi_NE.m_BitValue);
        AddNewInfo(pi_Info);
    }   
    
    /**
     * Get a label for the current entity.
     * In case the entity has multiple primitive type, the first one (given NETypes order) is returned
     * @return a String label
     */
    public String GetLabel(NamedEntityTypeEnumI[] pi_Types) {
        for (int i = 0; i != pi_Types.length; ++i) {
            if (m_BitValue.get(pi_Types[i].BitPos())) {
                if (pi_Types[i].Label() != null) return pi_Types[i].Label();
            }
        }
        return null;
    }
    public ArrayList<String> GetAllLabels(NamedEntityTypeEnumI[] pi_Types) {
        ArrayList<String> alTypes = new ArrayList<String>();  
        for (int i = 0; i != pi_Types.length; ++i) {
            if (m_BitValue.get(pi_Types[i].BitPos())) {
                if (pi_Types[i].Label() != null) alTypes.add(pi_Types[i].Label());
            }
        }
        return alTypes;
    }
    
    /**
     * Get explanations about this named entity
     * @return explanation string, HTML formatted
     */
    public String GetInfo() {
        return m_StrInfo.toString();
    }

    protected void AddLeadingInfo(String pi_Info) {
        if (m_StrInfo.length() == 0 || (m_StrInfo.toString().indexOf(pi_Info) == -1 && pi_Info.indexOf(m_StrInfo.toString()) == -1)) {
            m_StrInfo.insert(0, pi_Info);
        }
    }
    
    protected void AddTrailingInfo(String pi_Info) {
        m_StrInfo.append(pi_Info);
    }
    
    protected void AddNewInfo(NamedEntityExplanation pi_Info) {
        if (pi_Info != null && (m_StrInfo.length() == 0 || (m_StrInfo.toString().indexOf(pi_Info.toString()) == -1 && pi_Info.toString().indexOf(m_StrInfo.toString()) == -1))) {
            m_StrInfo.append("<li>"+pi_Info.toString()+"</li>");
        }        
    }

    public void WipeInfo() {
        m_StrInfo = new StringBuffer();
    }
    
    /**
     * Check if this entity has the given primitive type
     * @param pi_Type primitive type
     * @return true if the current entity has the primitive type
     */
    public boolean IsA(NamedEntityTypeEnumI pi_Type) {
        BitSet biType = new BitSet(m_TagSetSize);
        biType.set(pi_Type.BitPos());
        return m_BitValue.intersects(biType);
    }
    
    /**
     * Check if this entity dtarts an expression (equivalent to IsA(NETypes.START))
     * @return true if IsA(Start)
     */
    public boolean IsStart() {
        return IsA(NamedEntityTypeEnum.START);
    }
    
    /**
     * Check if this entity ends an expression (equivalent to IsA(NETypes.END))
     * @return true if IsA(END)
     */
    public boolean IsEnd() {
        return IsA(NamedEntityTypeEnum.END);
    }
    
    /**
     * Check if this entity has no primitive types
     * @return true if no tag
     */
    public boolean HasNoTag() {
        return m_BitValue.cardinality() == 0;
    }

    /**
     * Get the number of primitive types declared in this entity
     * @return number fo primitive types
     */
    public int TypeCount() {
        return m_BitValue.cardinality();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object pi_Obj) {
        boolean bEqual = false;
        if (m_BitValue.equals(((NamedEntityType)pi_Obj).m_BitValue) &&
            m_TagSetSize == ((NamedEntityType)pi_Obj).m_TagSetSize) {
                bEqual = true;
        }
        return bEqual;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int result = HashCodeUtil.SEED;
        result = HashCodeUtil.hash( result, m_BitValue );
        result = HashCodeUtil.hash( result, m_TagSetSize );
        result = HashCodeUtil.hash( result, m_StrInfo );
        return result;
    }    
    
    
    private BitSet                      m_BitValue;
    private int                         m_TagSetSize;
    private StringBuffer                m_StrInfo;
    
}
