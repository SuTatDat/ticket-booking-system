#!/bin/bash

# ===========================================
# Database Initialization Script
# ===========================================

set -e

# MySQL connection details
MYSQL_HOST="${MYSQL_HOST:-localhost}"
MYSQL_PORT="${MYSQL_PORT:-3306}"
MYSQL_ROOT_PASSWORD="${MYSQL_ROOT_PASSWORD:-root}"
MYSQL_USER="${MYSQL_USER:-ticketuser}"
MYSQL_PASSWORD="${MYSQL_PASSWORD:-ticketpass}"

echo "Initializing databases..."

# Wait for MySQL to be ready
echo "Waiting for MySQL to be ready..."
until mysql -h"$MYSQL_HOST" -P"$MYSQL_PORT" -uroot -p"$MYSQL_ROOT_PASSWORD" -e "SELECT 1" > /dev/null 2>&1; do
    echo "MySQL is unavailable - sleeping"
    sleep 2
done
echo "MySQL is ready!"

# Create databases
mysql -h"$MYSQL_HOST" -P"$MYSQL_PORT" -uroot -p"$MYSQL_ROOT_PASSWORD" <<EOF
-- Create databases for each service
CREATE DATABASE IF NOT EXISTS inventory_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS booking_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS payment_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user and grant privileges
CREATE USER IF NOT EXISTS '${MYSQL_USER}'@'%' IDENTIFIED BY '${MYSQL_PASSWORD}';

GRANT ALL PRIVILEGES ON inventory_db.* TO '${MYSQL_USER}'@'%';
GRANT ALL PRIVILEGES ON booking_db.* TO '${MYSQL_USER}'@'%';
GRANT ALL PRIVILEGES ON payment_db.* TO '${MYSQL_USER}'@'%';

FLUSH PRIVILEGES;

SELECT 'Databases created successfully!' as Status;
EOF

echo "Database initialization completed!"
