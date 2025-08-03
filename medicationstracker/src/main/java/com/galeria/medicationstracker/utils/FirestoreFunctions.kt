package com.galeria.medicationstracker.utils

import com.google.firebase.firestore.FirebaseFirestore

class FirestoreFunctions {
    object FirestoreService {
        
        val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    }
}
