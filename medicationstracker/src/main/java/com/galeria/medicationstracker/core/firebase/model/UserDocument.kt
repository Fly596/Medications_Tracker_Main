package com.galeria.medicationstracker.core.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class UserDocument(
  @DocumentId
  val id: String,
  var name: String = "",
  var email: String = "",
  var weight: Float? = null,
  var height: Float? = null,
  var dateOfBirth: Timestamp? = null,
)
