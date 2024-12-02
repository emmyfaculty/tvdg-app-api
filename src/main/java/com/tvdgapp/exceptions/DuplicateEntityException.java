package com.tvdgapp.exceptions;


import com.tvdgapp.utils.ErrorMsgUtils;

public class DuplicateEntityException extends RuntimeException {

//  private final static String ERROR_CODE = "409";
//    /**
//   *
//   */
//  private static final long serialVersionUID = 1L;
//
//    public DuplicateEntityException(String errorCode, String message) {
//        super(errorCode);
//    }
//
//    public DuplicateEntityException(String message) {
//        super(message);
//    }
//
//    public DuplicateEntityException(EntityType entityType, String id) {
//      super(ErrorMsgUtils.formatMsg(entityType.name(),ExceptionType.DUPLICATE_ENTITY.getValue(),id));
//    }

  private static final long serialVersionUID = 1L;
  private final static String ERROR_CODE = "409";
  private final String entityType;
  private final String fieldName;
  private final String fieldValue;

  public DuplicateEntityException(String entityType, String fieldName, String fieldValue) {
    super(String.format("%s already exists with %s: %s", entityType, fieldName, fieldValue));
    this.entityType = entityType;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }

  public DuplicateEntityException(EntityType entityType, String fieldName, String fieldValue) {
    this(entityType.name(), fieldName, fieldValue);
  }

  public String getEntityType() {
    return entityType;
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getFieldValue() {
    return fieldValue;
  }
}
