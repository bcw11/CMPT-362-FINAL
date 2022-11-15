package com.G3.kalendar.database.user

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    init {
        viewModelScope.launch {
            _users.value = repository.getAll()
        }
    }

    fun insert(user: User) {
        repository.insert(user)
    }

    fun getUser(id: String): User? {
        return repository.getUser(id)
    }

    fun authenticate(email: String, password: String): User? {
        return repository.authenticate(email, password)
    }
}

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java))
            return UserViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}