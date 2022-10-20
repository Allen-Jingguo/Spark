import { PageContainer } from '@ant-design/pro-components';
import {
  Space,
  Table,
  Tag,
  Button,
  Col,
  Form,
  Input,
  Row,
  Select,
  UpOutlined,
  DownOutlined,
  message,
  Popconfirm,
} from 'antd';
import {
  ProFormDatePicker,
  ProFormDateRangePicker,
  ProFormSelect,
  ProFormText,
  QueryFilter,
} from '@ant-design/pro-components';
import { Card } from 'antd';
import React, { useState, useEffect } from 'react';
import fomatterApi from '@/services/spark/fomatterApi';
import FormatterCreateModal from '@/pages/Formatter/formatterCreateModal';

const view = (record) => {
  fomatterApi.view({ ...record }).then((result) => {
    console.log(result);
    if (result) {
      message.success('start success!');
    }
  });
  message.success('operate successfully');
};

const disable = (record) => {
  console.log(record);
  fomatterApi.disable({ ...record }).then((result) => {
    console.log(result);
    if (result) {
      message.success('operate successfully!');
    }
  });
};

const cancel = (e) => {
  console.log(e);
  message.success('Canceled');
};

const columns = [
  // {
  //   title: 'Id',
  //   dataIndex: 'id',
  //   disable: true,
  // },
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
    render: (text) => <a>{text}</a>,
  },

  {
    title: 'Type',
    dataIndex: 'formatterType',
  },

  {
    title: 'Created Time',
    dataIndex: 'createdTime',
  },

  {
    title: 'Action',
    key: 'action',
    render: (_, record) => (
      <Space size="middle">
        <Button
          type="primary"
          onClick={() => {
            message.success('Wait for the implementation !');
          }}
        >
          Detail
        </Button>
        <Popconfirm
          title="Are you sure to start this flow?"
          onConfirm={() => disable(record)}
          onCancel={cancel}
          okText="Yes"
          cancelText="No"
        >
          <Button type="primary">disable</Button>
        </Popconfirm>
      </Space>
    ),
  },
];

const data = [
  {
    key: '1',
    id: '1',
    name: 'Ord  Extract Static Data Flow',
    inboundName: 'Ord JDBC Inbound Name',
    parserName: 'JDBCParser',
    filterName: 'NA',
    keyMapperName: 'KeyValueMapper',
    formatterName: 'JDBCFormatter',
    outboundName: 'WSS JDBC Oubound Name',
  },
  {
    key: '2',
    id: '2',
    name: 'SDR Static Data Flow',
    inboundName: 'SDR JDBC Inbound Name',
    parserName: 'JDBCParser',
    filterName: 'NA',
    keyMapperName: 'KeyValueMapper',
    formatterName: 'NA',
    outboundName: 'Ord JDBC Oubound Name',
  },
];

const FormatterList = () => {
  const [form] = Form.useForm();
  const [tableList, setTableList] = useState([]);
  const [showCreateModal, setShowCreateModal] = useState(false);

  useEffect(() => {
      fomatterApi.getList().then((resp) => {
        if (resp.success) {
          setTableList(resp.data);
        }
      });
  }, []);

  const doSearch = (values) => {
    fomatterApi.getList(values).then((resp) => {
      if (resp.success) {
        setTableList(resp.data);
      }
    });
  };

  const closeCreateModal = (success) => {
    if (success) {
      fomatterApi.getList().then((resp) => {
        if (resp.success) {
          setTableList(resp.data);
        }
      });
    }
    setShowCreateModal(false);
  };

  return (
    <PageContainer>
      <Card
        style={{
          borderRadius: 8,
        }}
        bodyStyle={{
          backgroundImage:
            'radial-gradient(circle at 97% 10%, #EBF2FF 0%, #F5F8FF 28%, #EBF1FF 124%)',
        }}
      >
        <div>
          <Form
            form={form}
            name="advanced_search"
            className="ant-advanced-search-form"
            onFinish={doSearch}
          >
            <Row gutter={24}>
              <Col>
                <Form.Item
                  name="name"
                  label="Name"
                  // rules={[{ required: true, message: 'Missing name' }]}
                >
                  <Input />
                </Form.Item>
              </Col>
              <Col>
                <Form.Item name="type" label="Type">
                  <Select
                    options={[
                      { label: 'Default', value: 'default' },
                      { label: 'Transation', value: 'Transation' },
                    ]}
                    style={{ width: 150 }}
                  />
                </Form.Item>
              </Col>

              <Col
                //span={24}
                style={{
                  textAlign: 'left',
                }}
              >
                <Button type="primary" htmlType="submit">
                  Search
                </Button>
                <Button
                  style={{ marginLeft: 10 }}
                  type="primary"
                  onClick={() => {
                    form.resetFields();
                  }}
                >
                  Clear
                </Button>
                <Button
                  style={{ marginLeft: 10 }}
                  type="primary"
                  onClick={() => {
                    setShowCreateModal(true);
                  }}
                >
                  Create New
                </Button>
              </Col>
            </Row>
          </Form>
        </div>
        <div style={{ marginTop: 20 }}>
          <Table columns={columns} dataSource={tableList} pagination={true} />
        </div>
      </Card>
      {showCreateModal && <FormatterCreateModal closeCreateModal={closeCreateModal} />}
    </PageContainer>
  );
};
export default FormatterList;
