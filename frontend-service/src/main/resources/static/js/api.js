
const API_BASE_URL = 'http://localhost:8090';

const TOKEN_KEY = 'authToken';
const USER_KEY = 'currentUser';

function getAuthToken() {
    return localStorage.getItem(TOKEN_KEY);
}

function setAuthToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
}

function getCurrentUser() {
    const userStr = localStorage.getItem(USER_KEY);
    return userStr ? JSON.parse(userStr) : null;
}

function setCurrentUser(user) {
    localStorage.setItem(USER_KEY, JSON.stringify(user));
}

function clearAuth() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
}

function isLoggedIn() {
    return !!getAuthToken();
}

function getUserRole() {
    const user = getCurrentUser();
    if (user && user.role) {
        return user.role;
    }
    
    const token = getAuthToken();
    if (token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.role || null;
        } catch (e) {
            console.error('Error extracting role from token:', e);
        }
    }
    
    return null;
}

function isAdmin() {
    const role = getUserRole();
    return role === 'ADMIN';
}

function getUserId() {
    const user = getCurrentUser();
    if (user && user.id) {
        return user.id;
    }
    
    const token = getAuthToken();
    if (token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.userId || null;
        } catch (e) {
            console.error('Error extracting userId from token:', e);
        }
    }
    
    return null;
}

async function apiCall(endpoint, options = {}) {
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const token = getAuthToken();
    if (token) {
        defaultOptions.headers['Authorization'] = `Bearer ${token}`;
    }

    const finalOptions = {
        ...defaultOptions,
        ...options,
        headers: {
            ...defaultOptions.headers,
            ...(options.headers || {}),
        },
    };

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, finalOptions);
        
        if (response.status === 401) {
            clearAuth();
            window.location.href = '/login?error=unauthorized';
            return null;
        }

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || `HTTP error! status: ${response.status}`);
        }

        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            return await response.json();
        }

        return await response.text();
    } catch (error) {
        console.error('API call failed:', error);
        throw error;
    }
}

const authAPI = {
    async register(username, password, email) {
        return apiCall('/auth/register', {
            method: 'POST',
            body: JSON.stringify({ username, password, email, role: 'ADOPTER' }),
        });
    },

    async login(username, password) {
        const response = await apiCall('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ username, password }),
        });
        
        if (response && response.token) {
            setAuthToken(response.token);
            
            let userToStore = null;
            if (response.user) {
                userToStore = response.user;
                if (!userToStore.id) {
                    try {
                        const payload = JSON.parse(atob(response.token.split('.')[1]));
                        userToStore.id = payload.userId;
                    } catch (e) {
                        console.error('Error extracting userId from token:', e);
                    }
                }
            } else {
                try {
                    const payload = JSON.parse(atob(response.token.split('.')[1]));
                    userToStore = {
                        id: payload.userId,
                        username: payload.sub,
                        role: payload.role || 'ADOPTER'
                    };
                } catch (e) {
                    console.error('Error extracting user from token:', e);
                    userToStore = { username, role: 'ADOPTER' };
                }
            }
            
            setCurrentUser(userToStore);
            console.log('User stored after login:', userToStore);
        }
        
        return response;
    },

    async logout() {
        clearAuth();
        window.location.href = '/';
    },

    async getAllUsers() {
        return apiCall('/auth/users');
    },

    async getUserById(id) {
        return apiCall(`/auth/users/${id}`);
    },
};

const petAPI = {
    async getAll() {
        return apiCall('/api/pets');
    },

    async getById(id) {
        return apiCall(`/api/pets/${id}`);
    },

    async create(petData) {
        return apiCall('/api/pets', {
            method: 'POST',
            body: JSON.stringify(petData),
        });
    },

    async createWithPhoto(formData) {
        return fetch(`${API_BASE_URL}/api/pets`, {
            method: 'POST',
            body: formData,
        }).then(res => res.json());
    },

    async update(id, petData) {
        return apiCall(`/api/pets/${id}`, {
            method: 'PUT',
            body: JSON.stringify(petData),
        });
    },

    async updateWithPhoto(id, formData) {
        const token = getAuthToken();
        const headers = {};
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }
        
        return fetch(`${API_BASE_URL}/api/pets/${id}`, {
            method: 'PUT',
            headers: headers,
            body: formData,
        }).then(async res => {
            if (res.status === 401) {
                clearAuth();
                window.location.href = '/login?error=unauthorized';
                return null;
            }
            if (!res.ok) {
                const errorText = await res.text();
                throw new Error(errorText || `HTTP error! status: ${res.status}`);
            }
            return res.json();
        });
    },

    async delete(id) {
        return apiCall(`/api/pets/${id}`, {
            method: 'DELETE',
        });
    },
};

const adoptionAPI = {
    async getAll(userId = null) {
        const url = userId ? `/api/adoptions?userId=${userId}` : '/api/adoptions';
        return apiCall(url);
    },

    async getById(id) {
        return apiCall(`/api/adoptions/${id}`);
    },

    async create(userId, petId, appointmentDateTime) {
        const url = `/api/adoptions?userId=${userId}&petId=${petId}&appointmentDateTime=${appointmentDateTime}`;
        return apiCall(url, {
            method: 'POST',
        });
    },

    async updateStatus(id, status) {
        return apiCall(`/api/adoptions/${id}/status?status=${status}`, {
            method: 'PUT',
        });
    },
};

const notificationAPI = {
    async getByUserId(userId, unreadOnly = false) {
        const url = `/api/notifications?userId=${userId}${unreadOnly ? '&unreadOnly=true' : ''}`;
        return apiCall(url);
    },

    async getById(id) {
        return apiCall(`/api/notifications/${id}`);
    },

    async markAsRead(id) {
        return apiCall(`/api/notifications/${id}/read`, {
            method: 'PUT',
        });
    },
};


function showError(message, containerId = 'errorContainer') {
    const container = document.getElementById(containerId);
    if (container) {
        container.innerHTML = `<div style="color: red; text-align: center; padding: 10px; background: #ffebee; border-radius: 8px; margin: 10px 0;">${message}</div>`;
        container.style.display = 'block';
    } else {
        alert(message);
    }
}

function showSuccess(message, containerId = 'successContainer') {
    const container = document.getElementById(containerId);
    if (container) {
        container.innerHTML = `<div style="color: green; text-align: center; padding: 10px; background: #e8f5e9; border-radius: 8px; margin: 10px 0;">${message}</div>`;
        container.style.display = 'block';
    } else {
        alert(message);
    }
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
}
function requireAdmin() {
    if (!isLoggedIn() || !isAdmin()) {
        window.location.href = '/login?error=unauthorized';
        return false;
    }
    return true;
}

function requireAuth() {
    if (!isLoggedIn()) {
        window.location.href = '/login?error=unauthorized';
        return false;
    }
    return true;
}

function getPetImageUrl(photoPath) {
    if (!photoPath) {
        console.warn('No photoPath provided, using default image');
        return '/default-pet.png'; 
    }
    
    if (photoPath.startsWith('http://') || photoPath.startsWith('https://')) {
        return photoPath;
    }
    
    if (photoPath.startsWith('/uploads/')) {
        const fullUrl = `${API_BASE_URL}${photoPath}`;
        console.log('Constructed image URL:', fullUrl, 'from photoPath:', photoPath);
        return fullUrl;
    }
    
    const fullUrl = `${API_BASE_URL}${photoPath.startsWith('/') ? photoPath : '/' + photoPath}`;
    console.log('Constructed image URL (fallback):', fullUrl, 'from photoPath:', photoPath);
    return fullUrl;
}



