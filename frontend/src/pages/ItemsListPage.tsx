import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  SimpleGrid, Card, Image, Text, Badge, Group, Title,
  Container, Loader, Center, Alert, Button,
} from '@mantine/core'
import { getItems } from '@/api/items'
import type { Item } from '@/types/item'
import { useCartStore } from '@/store/useCartStore'

export default function ItemsListPage() {
  const [items, setItems] = useState<Item[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const navigate = useNavigate()
  const addItem = useCartStore(s => s.addItem)

  useEffect(() => {
    getItems()
      .then(setItems)
      .catch(() => setError('Failed to load products. Is the backend running?'))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <Center h="100vh"><Loader size="xl" /></Center>
  if (error) return <Center h="100vh"><Alert color="red" title="Error">{error}</Alert></Center>

  return (
    <Container size="xl" py="xl">
      <Title order={1} mb="xl">Products</Title>
      <SimpleGrid cols={{ base: 1, sm: 2, md: 3, lg: 4 }} spacing="lg">
        {items.map(item => (
          <Card
            key={item.id}
            shadow="sm"
            padding="md"
            radius="md"
            withBorder
            style={{ cursor: 'pointer' }}
            onClick={() => navigate(`/items/${item.id}`)}
          >
            <Card.Section>
              <Image src={item.imageUrl} height={200} alt={item.name} />
            </Card.Section>
            <Group justify="space-between" mt="md" mb="xs">
              <Text fw={600} lineClamp={1} style={{ flex: 1 }}>{item.name}</Text>
              <Badge variant="light" color="blue" size="sm">{item.category}</Badge>
            </Group>
            <Text size="xl" fw={700} c="blue">
              ${item.price.toFixed(2)}
            </Text>
            <Text size="sm" c="dimmed" mt={4}>
              {item.stock > 0 ? `${item.stock} in stock` : 'Out of stock'}
            </Text>
            <Button
              fullWidth
              mt="sm"
              disabled={item.stock === 0}
              onClick={e => { e.stopPropagation(); addItem(item.id, 1) }}
            >
              Add to Cart
            </Button>
          </Card>
        ))}
      </SimpleGrid>
    </Container>
  )
}
