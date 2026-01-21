package com.ticketbooking.booking.exception;

/**
 * Exception thrown when optimistic locking fails due to version mismatch.
 */
public class OptimisticLockException extends RuntimeException {
    
    private final String entityName;
    private final Long entityId;
    
    public OptimisticLockException(String entityName, Long entityId) {
        super(String.format("Optimistic lock conflict for %s with ID: %d. " +
                "The entity was modified by another transaction.", entityName, entityId));
        this.entityName = entityName;
        this.entityId = entityId;
    }
    
    public String getEntityName() {
        return entityName;
    }
    
    public Long getEntityId() {
        return entityId;
    }
}
