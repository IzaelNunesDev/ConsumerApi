#!/usr/bin/env python3
"""
Script de teste para verificar a integração com a API RotaFácil
"""

import requests
import json
from datetime import datetime

# Configuração da API
BASE_URL = "https://projetodepersistenciatp3.onrender.com"
API_VERSION = "/api/v1"

def test_api_health():
    """Testa se a API está online"""
    try:
        response = requests.get(f"{BASE_URL}/")
        print(f"✅ API Health Check: {response.status_code}")
        print(f"Response: {response.json()}")
        return True
    except Exception as e:
        print(f"❌ API Health Check Failed: {e}")
        return False

def test_login_endpoint():
    """Testa o endpoint de login"""
    try:
        # Dados de teste (você pode criar um usuário primeiro)
        login_data = {
            "email": "teste@exemplo.com",
            "senha": "123456"
        }
        
        response = requests.post(
            f"{BASE_URL}{API_VERSION}/auth/login",
            json=login_data,
            headers={"Content-Type": "application/json"}
        )
        
        print(f"🔐 Login Test: {response.status_code}")
        
        if response.status_code == 200:
            data = response.json()
            print(f"✅ Login successful!")
            print(f"Token: {data.get('access_token', 'N/A')[:20]}...")
            print(f"User: {data.get('user_info', {}).get('nome', 'N/A')}")
            return data.get('access_token')
        else:
            print(f"❌ Login failed: {response.text}")
            return None
            
    except Exception as e:
        print(f"❌ Login Test Failed: {e}")
        return None

def test_register_endpoint():
    """Testa o endpoint de registro"""
    try:
        # Dados de teste
        register_data = {
            "nome_completo": "Aluno Teste",
            "email": f"aluno.teste.{datetime.now().strftime('%Y%m%d%H%M%S')}@exemplo.com",
            "senha": "123456",
            "matricula": f"2024{datetime.now().strftime('%Y%m%d%H%M%S')}",
            "telefone": "11999999999"
        }
        
        response = requests.post(
            f"{BASE_URL}{API_VERSION}/auth/register/aluno",
            json=register_data,
            headers={"Content-Type": "application/json"}
        )
        
        print(f"📝 Register Test: {response.status_code}")
        
        if response.status_code == 200:
            data = response.json()
            print(f"✅ Registration successful!")
            print(f"User ID: {data.get('user_info', {}).get('id', 'N/A')}")
            return data.get('access_token')
        else:
            print(f"❌ Registration failed: {response.text}")
            return None
            
    except Exception as e:
        print(f"❌ Register Test Failed: {e}")
        return None

def test_protected_endpoint(token):
    """Testa um endpoint protegido"""
    if not token:
        print("❌ No token available for protected endpoint test")
        return False
        
    try:
        headers = {
            "Authorization": f"Bearer {token}",
            "Content-Type": "application/json"
        }
        
        # Testa o endpoint /me
        response = requests.get(
            f"{BASE_URL}{API_VERSION}/auth/me",
            headers=headers
        )
        
        print(f"🔒 Protected Endpoint Test: {response.status_code}")
        
        if response.status_code == 200:
            data = response.json()
            print(f"✅ Protected endpoint successful!")
            print(f"User: {data.get('nome', 'N/A')}")
            return True
        else:
            print(f"❌ Protected endpoint failed: {response.text}")
            return False
            
    except Exception as e:
        print(f"❌ Protected Endpoint Test Failed: {e}")
        return False

def test_routes_endpoint():
    """Testa o endpoint de rotas"""
    try:
        response = requests.get(f"{BASE_URL}{API_VERSION}/rotas/ativas")
        
        print(f"🛣️ Routes Test: {response.status_code}")
        
        if response.status_code == 200:
            routes = response.json()
            print(f"✅ Routes endpoint successful!")
            print(f"Found {len(routes)} active routes")
            return True
        else:
            print(f"❌ Routes endpoint failed: {response.text}")
            return False
            
    except Exception as e:
        print(f"❌ Routes Test Failed: {e}")
        return False

def main():
    """Executa todos os testes"""
    print("🚀 Iniciando testes de integração com a API RotaFácil")
    print("=" * 60)
    
    # Teste 1: Health Check
    if not test_api_health():
        print("❌ API não está disponível. Abortando testes.")
        return
    
    print("\n" + "=" * 60)
    
    # Teste 2: Registro
    token = test_register_endpoint()
    
    print("\n" + "=" * 60)
    
    # Teste 3: Login (se o registro falhou, tenta login com credenciais existentes)
    if not token:
        token = test_login_endpoint()
    
    print("\n" + "=" * 60)
    
    # Teste 4: Endpoint protegido
    if token:
        test_protected_endpoint(token)
    
    print("\n" + "=" * 60)
    
    # Teste 5: Rotas
    test_routes_endpoint()
    
    print("\n" + "=" * 60)
    print("✅ Testes concluídos!")
    print("\n📱 Agora você pode testar a integração no Android!")

if __name__ == "__main__":
    main() 